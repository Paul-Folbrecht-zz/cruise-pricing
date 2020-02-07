package com.tst

import com.tst.Promotions.PromotionCode

case class Promotion(code: PromotionCode,
                     notCombinableWith: Seq[String])

case class PromotionCombo(promotionCodes: Seq[String])

object Promotions {
  type PromotionCode = String
}

class Promotions {
  def allCombinablePromotions(allPromotions: Seq[Promotion]): Seq[PromotionCombo] = {
    val result = allPromotions.flatMap(promotion => combinablePromotions(promotion.code, allPromotions, cull = false))
    cullSubsets(result) // Cull at the end only strictly as an optimization.
  }

  def combinablePromotions(promotionCode: PromotionCode,
                           allPromotions: Seq[Promotion],
                           cull: Boolean = true): Seq[PromotionCombo] = {
    val result = combinablePromotionsHelper(Set(promotionCode), allPromotions.toSet, Set.empty)
    if (cull) cullSubsets(result) else result.toSeq
  }

  private def combinablePromotionsHelper(codes: Set[PromotionCode],
                                         allPromotions: Set[Promotion],
                                         solution: Set[PromotionCombo]): Set[PromotionCombo] = {
    allPromotions.flatMap { promotion =>
      // The recursive base case is finding an exclusion - that terminates this iteration.
      if (promotion.notCombinableWith.toSet.intersect(codes).nonEmpty) solution + PromotionCombo(codes.toSeq.sorted)
      else combinablePromotionsHelper(codes + promotion.code, allPromotions.filterNot(_ == promotion), solution)
    }
  }

  private def cullSubsets(result: Iterable[PromotionCombo]): Seq[PromotionCombo] = {
    def isSubset(one: PromotionCombo, two: PromotionCombo): Boolean = one.promotionCodes.toSet.subsetOf(two.promotionCodes.toSet)

    result.foldLeft(Set[PromotionCombo]()) { case (set, combo) =>
      if (set.exists(isSubset(combo, _))) set
      else set + combo
    }.toSeq
  }
}

class PromotionsNonRecursive extends Promotions {
  override def combinablePromotions(promotionCode: PromotionCode,
                                    allPromotions: Seq[Promotion],
                                    cull: Boolean = true): Seq[PromotionCombo] = {
    var done = false
    var combos: Set[PromotionCombo] = Set.empty
    val rootPromotion = allPromotions.find(_.code == promotionCode)
    // We can remove the target promotion from the set immediately.
    var currentPromotions: Seq[Promotion] = allPromotions.filterNot(p => rootPromotion.exists(_.notCombinableWith.contains(p.code)))

    // Iterate, building a combo on each pass and ignoring/removing exclusions, until no exclusions remain.
    while (!done) {
      val (combo, exclusions): (Set[PromotionCode], Set[PromotionCode]) =
        currentPromotions.foldLeft((Set[PromotionCode](), Set[PromotionCode]())) { case ((comboSet, newExclusionsSet), promotion) =>
          if (promotion.notCombinableWith.toSet.intersect(comboSet).nonEmpty) (comboSet, newExclusionsSet + promotion.code)
          else (comboSet + promotion.code, newExclusionsSet)
        }

      combos = combos + PromotionCombo(combo.toSeq.sorted)
      // When no exclusions are encountered, we're done; else, remove the exclusions just encountered.
      if (exclusions.isEmpty) done = true
      else currentPromotions = currentPromotions.filter(p => p.notCombinableWith.toSet.intersect(exclusions).isEmpty || p.code == promotionCode)
    }

    combos.toSeq
  }
}