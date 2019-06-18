<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>A simple, clean, and responsive HTML invoice template</title>

    <script src="js/chartjs_2.8.0.js"><!-- empty --></script>
    <link rel="stylesheet" type="text/css" href="css/index.css">
</head>

<body>
<div class="invoice-box">
    <table cellpadding="0" cellspacing="0">
        <tr class="top">
            <td colspan="2">
                <table>
                    <tr>
                        <td class="title">
                            <img src="https://www.sparksuite.com/images/logo.png" style="width:100%; max-width:300px;">
                        </td>

                        <td>
                            Last Update: June 14, 2019<br>
                            Last item added: June 14, 2019<br>
                            Total items: 32 <br>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr class="information">
            <td colspan="2">
                <table>
                    <tr>
                        <td>
                            Current Balance: &euro; 15 256.00<br>
                            Most profitable category: Work<br>
                            Most expensive category: Appartment
                        </td>

                        <td>
                            Best month: &euro; 2 000 (June 2019)<br>
                            Worst month: &euro; 50 (May 2017)<br>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <canvas id="myChart"><!-- empty --></canvas>
            </td>
        </tr>

        <tr class="heading">
            <td>
                Account
            </td>

            <td>
                Current amount
            </td>
        </tr>

        <tr class="details">
            <td>
                Argenta Zichtrekening
            </td>

            <td>
                &euro; 1 000
            </td>
        </tr>
        <tr class="details">
            <td>
                Argenta Spaarrekening
            </td>

            <td>
                &euro; 14 952.53
            </td>
        </tr>

        <tr class="heading">
            <td>
                Category
            </td>

            <td>
                Result
            </td>
        </tr>

        <tr class="item">
            <td>
                Lening
            </td>

            <td>
                &euro; -300.00
            </td>
        </tr>

        <tr class="item">
            <td>
                Werk
            </td>

            <td>
                &euro; 2 075.00
            </td>
        </tr>

        <tr class="item last">
            <td>
                Domain name (1 year)
            </td>

            <td>
                $10.00
            </td>
        </tr>

        <tr class="total">
            <td></td>

            <td>
                Total: $385.00
            </td>
        </tr>
        <tr class="heading">
            <td>
                The five most profitable items are
            </td>

            <td>
                Result
            </td>
        </tr>

        <tr class="item">
            <td>
                Other
            </td>

            <td>
                &euro; 300.00
            </td>
        </tr>

        <tr class="item">
            <td>
                Werk
            </td>

            <td>
                &euro; 2 075.00
            </td>
        </tr>

        <tr class="item last">
            <td>
                Domain name (1 year)
            </td>

            <td>
                $10.00
            </td>
        </tr>

        <tr class="total">
            <td></td>

            <td>
                Total: $385.00
            </td>
        </tr>

        <tr class="heading">
            <td>
                The five most expensive items are
            </td>

            <td>
                Result
            </td>
        </tr>

        <tr class="item">
            <td>
                Other
            </td>

            <td>
                &euro; 300.00
            </td>
        </tr>

        <tr class="item">
            <td>
                Werk
            </td>

            <td>
                &euro; 2 075.00
            </td>
        </tr>

        <tr class="item last">
            <td>
                Domain name (1 year)
            </td>

            <td>
                $10.00
            </td>
        </tr>

        <tr class="total">
            <td></td>

            <td>
                Total: $385.00
            </td>
        </tr>

    </table>


    </table>

    <canvas id="bar-chart-grouped" width="800" height="450"></canvas>


    <div class="doughnut-chart-container">
        <canvas id="doughnut-chartcanvas-1"></canvas>
    </div>

    <canvas id="radar-chart" width="800" height="600"></canvas>

</div>

<script>
    var ctx = document.getElementById('myChart').getContext('2d');
    var chart = new Chart(ctx, {
        // The type of chart we want to create
        type: 'line',

        // The data for our dataset
        data: {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
            datasets: [{
                label: 'My First dataset',
                backgroundColor: 'rgb(255, 99, 132)',
                borderColor: 'rgb(255, 99, 132)',
                data: [0, 10, 5, 2, 20, 30, 45]
            }]
        },

        // Configuration options go here
        options: {}
    });
</script>


<script>
    //get the doughnut chart canvas
    var ctx1 = document.getElementById("doughnut-chartcanvas-1");

    //doughnut chart data
    var data1 = {
        labels: ["Cash", "Pensioensparen", "Argenta zichtrekening", "Argenta spaarrekening", "Maaltijdcheques"],
        datasets: [
            {
                label: "TeamA Score",
                data: [350, 2600, 872, 15500, 80],
                backgroundColor: [
                    "#DEB887",
                    "#A9A9A9",
                    "#DC143C",
                    "#F4A460",
                    "#2E8B57"
                ],
                borderColor: [
                    "#CDA776",
                    "#989898",
                    "#CB252B",
                    "#E39371",
                    "#1D7A46"
                ],
                borderWidth: [1, 1, 1, 1, 1]
            }
        ]
    };

    //options
    var options = {
        responsive: true,
        title: {
            display: true,
            position: "top",
            text: "Doughnut Chart",
            fontSize: 18,
            fontColor: "#111"
        },
        legend: {
            display: true,
            position: "bottom",
            labels: {
                fontColor: "#333",
                fontSize: 16
            }
        }
    };

    //create Chart class object
    var chart1 = new Chart(ctx1, {
        type: "doughnut",
        data: data1,
        options: options
    });

</script>

<script>
    new Chart(document.getElementById("bar-chart-grouped"), {
        type: 'bar',
        data: {
            labels: ["January", "February", "March", "April"],
            datasets: [
                {
                    label: "Incomes",
                    backgroundColor: "#3e95cd",
                    data: [133,221,783,2478]
                }, {
                    label: "Expenses",
                    backgroundColor: "#8e5ea2",
                    data: [408,547,675,734]
                }, {
                    label: "Result",
                    backgroundColor: "#8e5ea2",
                    data: [308,247,175,534]
                }
            ]
        },
        options: {
            title: {
                display: true,
                text: 'Monthly result for 2019'
            }
        }
    });


</script>

<script>
    new Chart(document.getElementById("radar-chart"), {
        type: 'radar',
        data: {
            labels: ["January", "February", "March", "April", "May"],
            datasets: [
                {
                    label: "Incomes",
                    fill: true,
                    backgroundColor: "rgba(179,181,198,0.2)",
                    borderColor: "rgba(179,181,198,1)",
                    pointBorderColor: "#fff",
                    pointBackgroundColor: "rgba(179,181,198,1)",
                    data: [8.77,55.61,21.69,6.62,6.82]
                }, {
                    label: "Expenses",
                    fill: true,
                    backgroundColor: "rgba(255,99,132,0.2)",
                    borderColor: "rgba(255,99,132,1)",
                    pointBorderColor: "#fff",
                    pointBackgroundColor: "rgba(255,99,132,1)",
                    pointBorderColor: "#fff",
                    data: [25.48,54.16,7.61,8.06,4.45]
                }, {
                    label: "Result",
                    fill: true,
                    backgroundColor: "rgba(255,99,132,0.2)",
                    borderColor: "rgba(255,99,132,1)",
                    pointBorderColor: "#fff",
                    pointBackgroundColor: "rgba(255,99,132,1)",
                    pointBorderColor: "#fff",
                    data: [27.48,24.16,12.61,16.06,8.45]
                }
            ]
        },
        options: {
            title: {
                display: true,
                text: 'Distribution in % of world population'
            }
        }
    });

</script>

</body>
</html>