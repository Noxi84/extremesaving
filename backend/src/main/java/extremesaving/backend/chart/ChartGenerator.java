package extremesaving.backend.chart;


import extremesaving.backend.dto.TotalsDto;

import java.io.IOException;

public interface ChartGenerator {

    void generateChartPng(TotalsDto totalsDto) throws IOException;
}