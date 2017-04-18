//以下为highcharts组件绘制直方图
function histogram(id, title, yAxisTitle, data, angle, hAlign) {
	var options = {
		chart : {
			renderTo : id,
			type : 'column',
			shadow : true,
			spacingLeft : 15,
			spacingRight : 15
		},
		colors : [ '#2f7ed8', '#f28f43', '#8bbc21', '#910000', '#1aadce',
				'#492970', '#0d233a', '#77a1e5', '#c42525', '#a6c96a' ],
		title : {
			text : title
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : [],
			labels : {
				rotation : angle,
				align : hAlign,
				style : {
					fontSize : '13px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : yAxisTitle
			}
		},
		legend : {
			layout : 'horizontal',
			backgroundColor : '#FFFFFF',
			align : 'center',
			verticalAlign : 'bottom',
			padding : 10,
			shadow : true,
			enabled : false
		},
		tooltip : {
			formatter : function() {
				return '' + this.x + ': ' + this.y;
			}
		},
		exporting : {
			enabled : false
		// 用来设置是否显示‘打印’，'导出'等功能按钮，不设置时默认为显示。
		},
		plotOptions : {
			column : {
				pointPadding : 0.1,
				borderWidth : 0
			}
		},
		series : []
	};
	// Split the lines
	var lines = data.split('&');
	// Iterate over the lines and add categories or series
	$.each(lines, function(lineNo, line) {
		var items = line.split(',');
		// header line containes categories
		if (lineNo == 0) {
			$.each(items, function(itemNo, item) {
				if (itemNo > 0)
					options.xAxis.categories.push(item);
			});
		}
		// the rest of the lines contain data with their name in the first
		// position
		else {
			var series = {
				data : []
			};
			$.each(items, function(itemNo, item) {
				if (itemNo == 0) {
					series.name = item;
				} else {
					series.data.push(parseFloat(item));
				}
			});
			options.series.push(series);
		}
	});
	// Create the chart
	var chart = new Highcharts.Chart(options);
}
// 以下为highcharts组件绘制折线图
function line(id, title, yAxisTitle, data) {
	var options = {
		chart : {
			renderTo : id,
			shadow : true,
			spacingLeft : 15,
			spacingRight : 15
		},
		colors : [ '#2f7ed8', '#f28f43', '#8bbc21', '#910000', '#1aadce',
				'#492970', '#0d233a', '#77a1e5', '#c42525', '#a6c96a' ],
		title : {
			text : title
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : [],
			labels : {
				rotation : -45,
				align : 'right',
				style : {
					fontSize : '13px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		},
		yAxis : {
			title : {
				text : yAxisTitle
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#808080'
			} ]
		},
		legend : {
			layout : 'horizontal',
			backgroundColor : '#FFFFFF',
			align : 'center',
			verticalAlign : 'bottom',
			padding : 10,
			shadow : true,
			enabled : false
		},
		tooltip : {
			formatter : function() {
				return '' + this.x + ': ' + this.y;
			}
		},
		exporting : {
			enabled : false
		// 用来设置是否显示‘打印’，'导出'等功能按钮，不设置时默认为显示。
		},
		series : []
	};
	// Split the lines
	var lines = data.split('&');
	// Iterate over the lines and add categories or series
	$.each(lines, function(lineNo, line) {
		var items = line.split(',');
		// header line containes categories
		if (lineNo == 0) {
			$.each(items, function(itemNo, item) {
				if (itemNo > 0)
					options.xAxis.categories.push(item);
			});
		}
		// the rest of the lines contain data with their name in the first
		// position
		else {
			var series = {
				data : []
			};
			$.each(items, function(itemNo, item) {
				if (itemNo == 0) {
					series.name = item;
				} else {
					series.data.push(parseFloat(item));
				}
			});
			options.series.push(series);
		}
	});
	// Create the chart
	var chart = new Highcharts.Chart(options);
}
// 以下为highcharts组件绘制饼图
function pie(id, title, tipName, value, showLegend) {
	var options = {
		chart : {
			renderTo : id,
			defaultSeriesType : 'pie',
			shadow : true,
			spacingLeft : 15,
			spacingRight : 15
		},
		title : {
			text : title
		},
		legend : {
			layout : 'vertical',
			backgroundColor : '#FFFFFF',
			align : 'right',
			verticalAlign : 'top',
			padding : 10,
			shadow : true,
			floating : true
		},
		tooltip : {
			pointFormat : '{series.name}: <b>{point.percentage:.2f}%</b>'
		},
		exporting : {
			enabled : false
		// 用来设置是否显示‘打印’，'导出'等功能按钮，不设置时默认为显示。
		},
		plotOptions : {
			pie : {
				allowPointSelect : true,
				cursor : 'pointer',
				dataLabels : {
					enabled : true,
					color : '#000000',
					connectorColor : '#000000',
					formatter : function() {
						return '<b>' + this.point.name + '</b>: '
								+ this.percentage.toFixed(2) + ' %';
					}
				},
				showInLegend : showLegend
			// 控制是否显示图例
			}
		}
	};
	options.series = [];
	var l = options.series.length;
	options.series[l] = {
		name : tipName,
		data : []
	};
	// Split the lines
	var lines = value.split('&');
	$.each(lines, function(lineNo, line) {
		var items = line.split(',');
		options.series[0].data.push({
			name : items[0],
			y : parseFloat(items[1])
		});
	});
	// Create the chart
	var chart = new Highcharts.Chart(options);
}
// 以下为highcharts组件绘制条状图
function bar(id, title, yAxisTitle, data) {
	var options = {
		chart : {
			renderTo : id,
			type : 'bar',
			shadow : true,
			spacingLeft : 15,
			spacingRight : 15
		},
		title : {
			text : title
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : []
		},
		yAxis : {
			min : 0,
			title : {
				text : yAxisTitle,
				align : 'high'
			},
			labels : {
				overflow : 'justify'
			}
		},
		plotOptions : {
			bar : {
				dataLabels : {
					enabled : true
				}
			}
		},
		legend : {
			layout : 'horizontal',
			backgroundColor : '#FFFFFF',
			align : 'center',
			verticalAlign : 'bottom',
			padding : 10,
			shadow : true,
			enabled : false
		},
		exporting : {
			enabled : false
		// 用来设置是否显示‘打印’，'导出'等功能按钮，不设置时默认为显示。
		},
		series : []
	};
	// Split the lines
	var lines = data.split('&');
	// Iterate over the lines and add categories or series
	$.each(lines, function(lineNo, line) {
		var items = line.split(',');
		// header line containes categories
		if (lineNo == 0) {
			$.each(items, function(itemNo, item) {
				if (itemNo > 0)
					options.xAxis.categories.push(item);
			});
		}
		// the rest of the lines contain data with their name in the first
		// position
		else {
			var series = {
				data : []
			};
			$.each(items, function(itemNo, item) {
				if (itemNo == 0) {
					series.name = item;
				} else {
					series.data.push(parseFloat(item));
				}
			});
			options.series.push(series);
		}
	});
	// Create the chart
	var chart = new Highcharts.Chart(options);
}