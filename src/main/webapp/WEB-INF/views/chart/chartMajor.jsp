<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/template/header.jsp" %>
<c:set var="CP" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DOMA</title>
<!-- bootstrap css -->
<link rel="stylesheet" href="${CP}/resources/css/bootstrap/bootstrap.css"> 

<%-- Additional CSS --%>
<link rel="stylesheet" href="${CP}/resources/css/bootstrap/bootstrap-ege.min.css">
<link rel="stylesheet" href="${CP}/resources/css/chart/chart.css"> 
<script src="${CP}/resources/js/common.js"></script>
<script src="${CP}/resources/js/jquery_3_7_1.js"></script>
<script src="${CP}/resources/js/chart/chart.js"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script type="text/javascript">
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
        fetch('${CP}/chart/chartDataMajor.do')
            .then(response => response.json())
            .then(data => {
                const dataTable = new google.visualization.DataTable();
                dataTable.addColumn('string', '사고 유형');
                dataTable.addColumn('number', '사망자 수');
                dataTable.addColumn('number', '부상자 수');
                dataTable.addColumn('number', '중상자 수');

                data.majorDead.forEach((item, index) => {
                    const accidentType = item.MAJOR_NAME;
                    const deaths = item.TOTAL_DEATHS;
                    const casualties = data.majorCasualties[index].TOTAL_CASUALTIES;
                    const seriously = data.majorSeriously[index].TOTAL_SERIOUSLY;
                    dataTable.addRow([accidentType, deaths, casualties, seriously]);
                });

                const options = {
                    title: '사고 유형별 사망자 수, 부상자 수 및 중상자 수',
                    chartArea: {width: '70%', height: '70%'},
                    hAxis: {
                        title: '사고 유형',
                        titleTextStyle: {color: '#333'}
                    },
                    vAxis: {
                        minValue: 0
                    },
                    seriesType: 'bars',
                    series: {
                        0: {type: 'bars', color: '#FF0000'},  // 사망자 수 - 빨간색 막대
                        1: {type: 'bars', color: '#FFFF00'},  // 부상자 수 - 노란색 막대
                        2: {type: 'line', color: '#0000FF'}   // 중상자 수 - 파란색 선
                    },
                    animation: {
                        startup: true,
                        duration: 1000,
                        easing: 'out'
                    },
                    tooltip: {isHtml: true},
                    legend: {position: 'bottom'}
                };

                const chart = new google.visualization.ComboChart(document.getElementById('comboChart'));
                chart.draw(dataTable, options);

                // 차트 클릭 이벤트 핸들러
                google.visualization.events.addListener(chart, 'select', function() {
                    const selectedItem = chart.getSelection()[0];
                    if (selectedItem) {
                        const accidentType = dataTable.getValue(selectedItem.row, 0);
                        alert(accidentType + '이 선택되었습니다.');
                    }
                });
            })
            .catch(error => console.error('차트 데이터 가져오기 오류:', error));
    }
</script>

 
</head>
<body>

    <div class="center-content">
        <h1>사고 유형별 사망자 수 및 부상자 수</h1>
    </div>
    
    <!-- 버튼 컨테이너 -->
<!-- 버튼 컨테이너 -->
<%@ include file="/WEB-INF/views/chart/chart_btn.jsp" %> 
    
    <!-- 콤보 차트 컨테이너 -->
    <div id="comboChart" style="width: 100%; height: 500px; margin: 0 auto;"></div>

</body>
<%@ include file="/WEB-INF/views/template/footer.jsp" %> 
</html>
