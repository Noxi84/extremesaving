package extremesaving.backend.chart;

import extremesaving.frontend.dto.TotalsDto;

import java.io.IOException;

public interface ChartGenerator {

    void generateChartPng(TotalsDto totalsDto) throws IOException;
}