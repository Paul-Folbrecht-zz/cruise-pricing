package com.tst

import com.tst.Promotions.PromotionCode

case class Promotion(code: PromotionCode,
                     notCombinableWith: Seq[String])

case class PromotionCombo(promotionCodes: Seq[String])

object Promotions {
  type PromotionCode = String
}

class Promotions {
  //  Promotion(P1, Seq(P3))     // P1 is not combinable with P3
  //  Promotion(P2, Seq(P4, P5)) // P2 is not combinable with P4 and P5
  //  Promotion(P3, Seq(P1))     // P3 is not combinable with P1
  //  Promotion(P4, Seq(P2))     // P4 is not combinable with P2
  //  Promotion(P5, Seq(P2))     // P5 is not combinable with P2

  //  Seq(
  //    PromotionCombo(Seq(P1, P2)),
  //    PromotionCombo(Seq(P1, P4, P5)),
  //    PromotionCombo(Seq(P2, P3)),
  //    PromotionCombo(Seq(P3, P4, P5))
  //  )
  def allCombinablePromotions(allPromotions: Seq[Promotion]): Seq[PromotionCombo] = {
    allPromotions.flatMap { promotion =>
      combinablePromotions(promotion.code, allPromotions)
    }
  }

  def combinablePromotions(promotionCode: String,
                           allPromotions: Seq[Promotion]): Set[PromotionCombo] = {
    def isSubset(one: PromotionCombo, two: PromotionCombo): Boolean = one.promotionCodes.toSet.subsetOf(two.promotionCodes.toSet)

    // Start with P1.
    // For all other promotions
    //   - if nothing in current set is on excl list, add it to current codes, call function again with new set of codes
    // If we get thru all without adding any, add current codes to solutions
    val result = combinablePromotions(Set(promotionCode), allPromotions.toSet, Set.empty)
    // Cull subsets
    result.foldLeft(Set[PromotionCombo]()) { case (set, combo) =>
      if (set.exists(isSubset(combo, _))) set
      else set + combo
    }
  }

  private def combinablePromotions(codes: Set[String],
                                   allPromotions: Set[Promotion],
                                   solution: Set[PromotionCombo]): Set[PromotionCombo] = {
    allPromotions.flatMap { promotion =>
      if (promotion.notCombinableWith.toSet.intersect(codes).isEmpty)
        combinablePromotions(codes + promotion.code, allPromotions.filterNot(_ == promotion), solution)
      else solution + PromotionCombo(codes.toSeq.sorted) // Should only add for a top-level call...
    }
  }
}
