package com.microservices.swotlyzer.swot.testutils;

import java.util.Collections;

import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotArea;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotLayoutTypes;

public class GenerateSwotAnalysis {

        public final static String SWOT_TITLE = "Swot Title";

        public static SwotAnalysis generateSwotAnalysis(Long owerId, String swotId) {
                var swotStrengthsArea = new SwotArea(
                                "Strengths",
                                "green",
                                Collections.emptySet());

                var swotOpportunitiesArea = new SwotArea(
                                "Opportunities",
                                "blue",
                                Collections.emptySet());

                var swotWeaknessesArea = new SwotArea(
                                "Weaknesses",
                                "red",
                                Collections.emptySet());

                var swotThreatsArea = new SwotArea(
                                "Threats",
                                "grey",
                                Collections.emptySet());

                return new SwotAnalysis(
                                swotId,
                                SWOT_TITLE,
                                SwotLayoutTypes.DEFAULT,
                                true,
                                swotStrengthsArea,
                                swotWeaknessesArea,
                                swotOpportunitiesArea,
                                swotThreatsArea,
                                owerId);
        }
}
