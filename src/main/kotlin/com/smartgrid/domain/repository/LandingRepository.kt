package com.smartgrid.domain.repository

import com.smartgrid.domain.model.*

interface LandingRepository {
    fun getLandingPageData(): LandingPageData
    fun getHero(): HeroSection
    fun getFeatures(): FeaturesSection
    fun getHowItWorks(): HowItWorksSection
    fun getStats(): StatsSection
    fun getCta(): CtaSection
    fun getFooter(): FooterSection
}
