package com.tst

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PromotionsSpec extends AnyFunSpec with Matchers {
  private val promotions = Seq(
    Promotion("P1", Seq("P3")),
    Promotion("P2", Seq("P4", "P5")),
    Promotion("P3", Seq("P1")),
    Promotion("P4", Seq("P2")),
    Promotion("P5", Seq("P2"))
  )

  protected def makePromotions(): Promotions = new Promotions

  describe("A cruise promotions system") {
    it("Should find all valid promotion combinations") {
      val result = makePromotions().allCombinablePromotions(promotions)
      result shouldBe Seq(
        PromotionCombo(Seq("P1", "P2")),
        PromotionCombo(Seq("P1", "P4", "P5")),
        PromotionCombo(Seq("P2", "P3")),
        PromotionCombo(Seq("P3", "P4", "P5"))
      )
    }

    it("Should find valid promotion combinations 1") {
      val result = makePromotions().combinablePromotions("P1", promotions)
      result shouldBe Seq(
        PromotionCombo(Seq("P1", "P2")),
        PromotionCombo(Seq("P1", "P4", "P5"))
      )
    }

    it("Should find valid promotion combinations 2") {
      val result = makePromotions().combinablePromotions("P3", promotions)
      result shouldBe Seq(
        PromotionCombo(Seq("P2", "P3")),
        PromotionCombo(Seq("P3", "P4", "P5"))
      )
    }
  }
}

class PromotionsNonRecursiveSpec extends PromotionsSpec {
  override protected def makePromotions(): Promotions = new PromotionsNonRecursive
}