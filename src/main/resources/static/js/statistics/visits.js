$.ajax({
    method: 'get',
    url: '/statistics/top10Visits.action'
}).then(response => {
    //top 10 uri 访问量统计
    let data = response.data;
    let myChart = echarts.init(document.getElementById('top10Visits'));
    let uris = data.map(function (item) {
        return item.uri;
    });
    let counts = data.map(function (item) {
        return item.count;
    });
    // 指定图表的配置项和数据
    let option = {
        title: {
            text: i18n_top10UriStatistics,
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {type: 'shadow'}
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: uris.reverse(),  // 反转数组以使最大值在顶部
            axisLabel: {
                interval: 0,
                rotate: 0  // 保持标签水平
            }
        },
        series: [{
            name: i18n_visits,
            type: 'bar',
            data: counts.reverse(),  // 反转数组以对应 URI 的顺序
            label: {
                show: true,
                position: 'insideRight'
            },
            itemStyle: {
                color: '#3398DB'
            }
        }]
    };
    // 使用指定的配置项和数据显示图表
    myChart.setOption(option);
    window.addEventListener('resize', function() {
        myChart.resize();
    });
});

$.ajax({
    method: 'get',
    url: '/statistics/top10Ip.action'
}).then(response => {
    //top 10 ip 访问量统计
    let data = response.data;
    let myChart = echarts.init(document.getElementById('top10Ip'));
    let ip = data.map(function (item) {
        return item.ip;
    });
    let counts = data.map(function (item) {
        return item.count;
    });
    // 指定图表的配置项和数据
    let option = {
        title: {
            text: i18n_top10IpStatistics
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {type: 'shadow'}
        },
        xAxis: {
            type: 'category',
            data: ip,
            axisLabel: {
                interval: 0,
                rotate: 30  // 如果 URI 较长，可以旋转标签以防重叠
            }
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            name: i18n_visits,
            type: 'bar',
            data: counts,
            label: {
                show: true,
                position: 'top'
            }
        }]
    };

    // 使用指定的配置项和数据显示图表
    myChart.setOption(option);
    window.addEventListener('resize', function() {
        myChart.resize();
    });
});

$(function() {
    $(".sortable").sortable({
        handle: ".card-header",
        placeholder: "ui-sortable-placeholder",
        tolerance: "pointer"
    });
    $(".sortable").disableSelection();
});