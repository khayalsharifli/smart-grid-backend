package com.smartgrid.domain.service

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.LandingRepository

class LandingService(private val landingRepository: LandingRepository) {

    fun getLandingPageData(): LandingPageData = landingRepository.getLandingPageData()

    fun getHero(): HeroSection = landingRepository.getHero()

    fun getFeatures(): FeaturesSection = landingRepository.getFeatures()

    fun getHowItWorks(): HowItWorksSection = landingRepository.getHowItWorks()

    fun getStats(): StatsSection = landingRepository.getStats()

    fun getCta(): CtaSection = landingRepository.getCta()

    fun getFooter(): FooterSection = landingRepository.getFooter()
}
