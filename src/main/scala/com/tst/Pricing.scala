package com.tst

import com.tst.Pricing._

case class Rate(rateCode: RateCode,
                rateGroup: RateGroup)

case class CabinPrice(cabinCode: RateGroup,
                      rateCode: RateCode,
                      price: BigDecimal)

case class BestGroupPrice(cabinCode: CabinCode,
                          rateCode: RateCode,
                          price: BigDecimal,
                          rateGroup: RateGroup)

object Pricing {
  type RateCode = String
  type RateGroup = String
  type CabinCode = String

}

class PricingEngine {
  def getBestGroupPrices(rates: Seq[Rate],
                         prices: Seq[CabinPrice]): Seq[BestGroupPrice] = {
    // CabinPrice has everything output needs save rateGroup, and that's determined directly from rateCode
    // Group CabinPrices by rateGroup (looked up from its code), sort by price, take the head, that's it
    val rateGroupsByCode: Map[RateCode, RateGroup] = rates.map(rate => (rate.rateCode, rate.rateGroup)).toMap
    val pricesByCabinAndRateGroup: Map[(CabinCode, RateGroup), Seq[CabinPrice]] =
      prices.groupBy(price => (price.cabinCode, rateGroupsByCode.getOrElse(price.rateCode, "")))
    pricesByCabinAndRateGroup.map { case ((cabinCode, rateGroup), pricesForGroup) =>
      val sorted: ((CabinCode, RateGroup), Seq[CabinPrice]) = ((cabinCode, rateGroup), pricesForGroup.sortBy(_.price))
      val bestPrice = sorted._2.head
      BestGroupPrice(cabinCode, bestPrice.rateCode, bestPrice.price, rateGroup)
    }.toSeq.sortBy(_.price)
  }
}
