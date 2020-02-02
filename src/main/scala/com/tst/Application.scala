package com.tst

object Application extends App {
  pricing()
  promotions()

  private def pricing(): Unit = {
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
    println(new PricingEngine().getBestGroupPrices(rates, prices))
  }

  private def promotions(): Unit = {
    val promotions = Seq(
      Promotion("P1", Seq("P3")),
      Promotion("P2", Seq("P4", "P5")),
      Promotion("P3", Seq("P1")),
      Promotion("P4", Seq("P2")),
      Promotion("P5", Seq("P2"))
    )
    println(new Promotions().allCombinablePromotions(promotions))
    println(new Promotions().combinablePromotions("P1", promotions))
    println(new Promotions().combinablePromotions("P3", promotions))
  }
}
