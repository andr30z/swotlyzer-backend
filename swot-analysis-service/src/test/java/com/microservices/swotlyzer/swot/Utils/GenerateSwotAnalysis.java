package com.microservices.swotlyzer.swot.Utils;

import com.microservices.swotlyzer.swot.analysis.service.models.SwotAnalysis;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotArea;
import com.microservices.swotlyzer.swot.analysis.service.models.SwotLayoutTypes;
import java.util.Collections;

public class GenerateSwotAnalysis {

  public static SwotAnalysis generateSwotAnalysis(Long owerId, String swotId) {
    var swotStrengthsArea = new SwotArea(
      "Strengths",
      "green",
      Collections.emptySet()
    );
    var swotOpportunitiesArea = new SwotArea(
      "Opportunities",
      "blue",
      Collections.emptySet()
    );
    var swotWeaknessesArea = new SwotArea(
      "Weaknesses",
      "red",
      Collections.emptySet()
    );
    var swotThreatsArea = new SwotArea(
      "Threats",
      "grey",
      Collections.emptySet()
    );
    var swotTitle = "Swot Title";
    return new SwotAnalysis(
      swotId,
      swotTitle,
      SwotLayoutTypes.DEFAULT,
      true,
      swotStrengthsArea,
      swotWeaknessesArea,
      swotOpportunitiesArea,
      swotThreatsArea,
      owerId
    );
  }
}
