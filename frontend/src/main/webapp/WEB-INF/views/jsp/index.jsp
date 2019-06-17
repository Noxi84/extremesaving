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
</body>
</html>
