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
    cullSubsets(result)
  }

  def combinablePromotions(promotionCode: String,
                           allPromotions: Seq[Promotion],
                           cull: Boolean = true): Seq[PromotionCombo] = {
    // Add input code to the solution
    // For all other promotions
    //   - if nothing in current set is on excl list, add it to current codes, call function again with new set of codes
    // If we get thru all without adding any, add current codes to solutions
    val result = combinablePromotionsHelper(Set(promotionCode), allPromotions.toSet, Set.empty)
    if (cull) cullSubsets(result) else result.toSeq
  }

  private def combinablePromotionsHelper(codes: Set[String],
                                         allPromotions: Set[Promotion],
                                         solution: Set[PromotionCombo]): Set[PromotionCombo] = {
    allPromotions.flatMap { promotion =>
      if (promotion.notCombinableWith.toSet.intersect(codes).isEmpty)
        combinablePromotionsHelper(codes + promotion.code, allPromotions.filterNot(_ == promotion), solution)
      else solution + PromotionCombo(codes.toSeq.sorted)
    }
  }

  private def cullSubsets(result: Iterable[PromotionCombo]): Seq[PromotionCombo] = {
    result.foldLeft(Set[PromotionCombo]()) { case (set, combo) =>
      if (set.exists(isSubset(combo, _))) set
      else set + combo
    }.toSeq
  }

  private def isSubset(one: PromotionCombo, two: PromotionCombo): Boolean =
    one.promotionCodes.toSet.subsetOf(two.promotionCodes.toSet) ||
      two.promotionCodes.toSet.subsetOf(one.promotionCodes.toSet) ||
      one.promotionCodes.toSet == two.promotionCodes.toSet
}
