package extremesaving.chart;


import extremesaving.dto.TotalsDto;

import java.io.IOException;

public interface ChartGenerator {

    void generateChartPng(TotalsDto totalsDto) throws IOException;
}