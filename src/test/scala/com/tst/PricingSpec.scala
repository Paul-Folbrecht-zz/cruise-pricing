package com.tst

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PricingSpec extends AnyFunSpec with Matchers {
  describe("A cruise pricing system") {
    it("Should find best group prices") {
      val rates = Seq(
        Rate("M1", "Military"),
        Rate("M2", "Military"),
        Rate("S1", "Senior"),
        Rate("S2", "Senior")
      )
      val prices = Seq(
        CabinPrice("CA", "M1", 200.00),
        CabinPrice("CA", "M2", 250.00),
        CabinPrice("CA", "S1", 225.00),
        CabinPrice("CA", "S2", 260.00),
        CabinPrice("CB", "M1", 230.00),
        CabinPrice("CB", "M2", 260.00),
        CabinPrice("CB", "S1", 245.00),
        CabinPrice("CB", "S2", 270.00)
      )
      val bestPrices = new PricingEngine().getBestGroupPrices(rates, prices)
      bestPrices shouldBe List(
        BestGroupPrice("CA", "M1", 200.0, "Military"),
        BestGroupPrice("CA", "S1", 225.0, "Senior"),
        BestGroupPrice("CB", "M1", 230.0, "Military"),
        BestGroupPrice("CB", "S1", 245.0, "Senior")
      )
    }

    it("Should find best group prices 2") {
      val rates = Seq(
        Rate("M1", "Military"),
        Rate("M2", "Military"),
        Rate("S1", "Senior"),
        Rate("S2", "Senior")
      )
      val prices = Seq(
        CabinPrice("CA", "M1", 200.00),
        CabinPrice("CA", "M2", 250.00),
        CabinPrice("CA", "S1", 225.00),
        CabinPrice("CA", "S2", 260.00),
        CabinPrice("CB", "M1", 230.00),
        CabinPrice("CB", "M2", 260.00),
        CabinPrice("CB", "S1", 245.00),
        CabinPrice("CB", "S2", 270.00),
        CabinPrice("CA", "M1", 550.00),
        CabinPrice("CA", "M2", 550.00),
        CabinPrice("CA", "S1", 550.00),
        CabinPrice("CA", "S2", 550.00),
      )
      val bestPrices = new PricingEngine().getBestGroupPrices(rates, prices)
      bestPrices shouldBe List(
        BestGroupPrice("CA", "M1", 200.0, "Military"),
        BestGroupPrice("CA", "S1", 225.0, "Senior"),
        BestGroupPrice("CB", "M1", 230.0, "Military"),
        BestGroupPrice("CB", "S1", 245.0, "Senior")
      )
    }

    it("Should find best group prices with missing rates") {
      val rates = Seq(
        Rate("M1", "Military")
      )
      val prices = Seq(
        CabinPrice("CA", "M1", 200.00),
        CabinPrice("CA", "M2", 250.00),
        CabinPrice("CA", "S1", 225.00),
        CabinPrice("CA", "S2", 260.00),
        CabinPrice("CB", "M1", 230.00),
        CabinPrice("CB", "M2", 260.00),
        CabinPrice("CB", "S1", 245.00),
        CabinPrice("CB", "S2", 270.00),
        CabinPrice("CA", "M1", 550.00),
        CabinPrice("CA", "M2", 550.00),
        CabinPrice("CA", "S1", 550.00),
        CabinPrice("CA", "S2", 550.00),
      )
      val bestPrices = new PricingEngine().getBestGroupPrices(rates, prices)
      bestPrices shouldBe List(
        BestGroupPrice("CA", "M1", 200.0, "Military"),
        BestGroupPrice("CA", "S1", 225.0, ""),
        BestGroupPrice("CB", "M1", 230.0, "Military"),
        BestGroupPrice("CB", "S1", 245.0, "")
      )
    }
  }

  it("Should find best group prices when none are present for a Rate Group") {
    val rates = Seq(
      Rate("M1", "Military"),
      Rate("M2", "Military"),
      Rate("S1", "Senior"),
      Rate("S2", "Senior")
    )
    val prices = Seq(
      CabinPrice("CA", "S1", 225.00),
      CabinPrice("CA", "S2", 260.00),
      CabinPrice("CB", "S1", 245.00),
      CabinPrice("CB", "S2", 270.00),
      CabinPrice("CA", "S1", 550.00),
      CabinPrice("CA", "S2", 550.00)
    )
    val bestPrices = new PricingEngine().getBestGroupPrices(rates, prices)
    bestPrices shouldBe List(
      BestGroupPrice("CA", "S1", 225.0, "Senior"),
      BestGroupPrice("CB", "S1", 245.0, "Senior")
    )
  }
}
