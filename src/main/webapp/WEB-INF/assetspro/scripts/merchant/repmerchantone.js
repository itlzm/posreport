var baseUrl = $("#baseUrl").val();
var funcID = "R0501";
var pageIndex = 1;
var pageSzie = 15;
var initdteTimeStart;
var initdteTimeEnd;

// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间


// 显示查询窗口
function fnBtnClickShowQuery() {

	if ($("#QueryDiv").css("display") == 'none') {
		$("#QueryDiv").css("display", "inline");
	} else {
		$("#QueryDiv").css("display", "none");
	}

}

// 查询按钮注册
function fnListeningActQuery() {
	$(document).ready(function() {
		$('#ActQuery').click(function() {
			// 开启编辑控件可用
			fnBtnClickShowQuery();
		});
	});
}

// 执行查询并关闭查询窗口
function fnBtnClickSetQuery() {
	$("#QueryDiv").css("display", "none");
	pageIndex = 1;
	var fullName = $("#fullName").val();
	var contractPerson = $("#contractPerson").val();
	console.log(fullName+":"+contractPerson);

	fnQueryData(fullName, contractPerson);

}

// 查询数据
function fnQueryData(fullName, contractPerson) {
	fnLoadingShowAndClose(true);
	
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/merchant/queryMerchantDetail/',
		data : {
			FullName : fullName,
			ContractPerson : contractPerson,
			PageIndex : pageIndex,
			PageSize : pageSzie
		},
		success : function(data) {
			// 明细数据信息
			var listData = data.listData;
			// 分页信息;
			var pageData = data.pageData;
			// 合计行
			var listDataSum = data.listDataSum;

			fnListTableData(listData, pageData,listDataSum);
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}

function fnListTableData(listData, pageData,listDataSum) {
	// 绑定明细列表信息
	//var len = listDataSum.length;
	var strHtml = "";
	var strColName = "";
	var strColNameValue = "";
	//for (var i = 0; i < len; i++) {
	for (var i = 0; i < listData.length; i++) {
		if (i == 0) {
			strColName += "<thead><tr>";
			strColName += "<th scope='col' style=\"text-align: center;\">行号 </th>";
			for (obj in listData[i]) {
				if (obj == "商家编码" ) {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "商家名称" ||  obj == "'电话") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 80px;padding-right: 80px;'>"
							+ obj + "</th>";
				}else if ( obj == "地址" ){
					strColName += "<th scope='col' style='text-align: center;padding-left: 50px;padding-right: 50px;'>"
						+ obj + "</th>";
				} else {
					strColName += "<th scope='col' style=\"text-align: center;\">"
							+ obj + "</th>";
				}
			}
			strColName += "</tr></thead>";
			break;
		}
	}	
	strColNameValue += "<tbody>";
	for (var i = 0; i < listData.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td>" + ((pageIndex - 1) * pageSzie + i + 1)
				+ "</td>"; // 明细行号列
		$.each(listData[i], function(key, value) {
			if (isfloat(value)) {

				strColNameValue += "<td align='right'>" + value
						+ "</td>";
			} else {
				strColNameValue += "<td>" + value + "</td>";
			}
		});
		strColNameValue += "</tr>";
	}

	

	strColNameValue += "</tbody>";

	strHtml += strColName;
	strHtml += strColNameValue;
	document.getElementById('listTableData').innerHTML = "";
	$('#listTableData').append(strHtml);

	var strPageHtml = "";
	strPageHtml += "<ul class=\"pagination pull-right\" id=\"dataPager\">";
	strPageHtml += "</ul>";
	document.getElementById('dataPagerDiv').innerHTML = "";
	$('#dataPagerDiv').append(strPageHtml);

	// 绑定分页信息;
	$('#dataPager').bootpag({
		paginationClass : 'pagination pagination-sm',
		next : '<i class="fa fa-angle-right"></i>',
		prev : '<i class="fa fa-angle-left"></i>',
		total : parseInt(pageData[0].totalPages),
		page : pageIndex,
		maxVisible : 6,
		leaps : false,
		firstLastUse : false,
		first : '<i class="fa fa-angle-double-left"></i>',
		last : '<i class="fa fa-angle-double-right"></i>'
	}).on('page', function(event, num) {
		pageIndex = num;
		var BrandName = "";
		var StoreName = "";
		var ChannelName = "";
		var Paymentkey = $("#setPaymentkey").select2("val");
		if (Paymentkey == "ALL") {
			Paymentkey = "";
		}
		fnQueryData(BrandName, StoreName, ChannelName, Paymentkey);
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {
	$("#fullName").val("");
	$("#contractPerson").val("");
}

// 注册下拉框值改变事件
/*
 *
function fnListeningCtrSelectChange() {
	// 所有品牌
	$("#setBrandName").on("change", function(e) {
		fnGetChannelNameByBrandName()
	});

}
 */


// 根据选择的品牌,查询该品牌下所有的渠道
/*
 * 
function fnGetChannelNameByBrandName() {
	var strBrandName = $("#setBrandName").select2("val"); // 品牌
	if (Object.prototype.toString.call(strBrandName) == "[object Object]") {
		strBrandName = "";
	}
	// 清空渠道下拉
	$("#setChannelName").select2("val", "");
	$("#setStoreName").select2("val", "");
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/brand/change/' + strBrandName,
		dataType : "json",
		success : function(data) {

			var strChannelName = JSON.stringify(eval(data.channelName));
			var strChannelNameTemp = "";
			if (typeof (strChannelName) != "undefined") {
				strChannelName = strChannelName.substring(1,
						strChannelName.length);
				strChannelNameTemp = '[{"id":"","text":"全部渠道"},'
						+ strChannelName;
			} else {
				strChannelNameTemp = '[{"id":"","text":"全部渠道"}]'

			}

			$("#setChannelName").select2("val", "");
			$("#setChannelName").select2({
				data : eval(strChannelNameTemp)
			});

			// fnBindQueryStoreViewData(data.storeName);

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}
 */


/*
 * 
var aryStoreList = "";
function fnInitForm() {

	// 支付方式
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/paymentkey',
		dataType : "json",
		success : function(data) {

			var strPaymentkey = JSON.stringify(eval(data.paymentkey));
			strPaymentkey = strPaymentkey.substring(1, strPaymentkey.length);
			var strPaymentkeyTemp = '[{"id":"ALL","text":"全部支付方式"},'
					+ strPaymentkey;

			$("#setPaymentkey").select2({
				data : eval(strPaymentkeyTemp)
			});
			$("#setPaymentkey").select2("val", "ALL");
		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

	var strStoreType = $("#setStoreType").val();
	
	// 获取授权的所有门店信息
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/brand/getStoreList?StoreType='+strStoreType,
		dataType : "json",
		async : false,
		success : function(data) {
			
			var strStore = "";
			var storeName = data.storeName;
			for (var i = 0; i < storeName.length; i++) {

				var ISLAST = storeName[i].ISLAST;
				var ID = storeName[i].ID;
				if (ISLAST == "Y") {
					strStore += ID + "|";
				}
			}

			if (strStore != "") {
				strStore = strStore.substr(0, strStore.length - 1);
			}
			$("#StoreListVal").val(strStore);
			aryStoreList = strStore;

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}
 */

/*
 * 
function fnStoreTree() {
	var asyncSettings = {
		type : 'iframe',
		closeable : false,
		padding : false,
		cache : false,
		url : baseUrl + '/queryStoreList'
	};
	$('#setStoreList').webuiPopover('destroy').webuiPopover(
			$.extend({}, asyncSettings));
}

fnStoreTree();
 */

/*
 * 
function fnGetQueryStoreTree(strBranCode,strBranName, treeText, strStoreTreeText, treeVal) {
	$("#BranCode").val(strBranCode);
	$("#setBrandName").val(strBranName);
	$("#setStoreListText").val(strStoreTreeText);
	$("#StoreListVal").val(treeVal);
}
 */


/*
 * 
function fnGetSelectStoreTree() {
	return $("#StoreListVal").val();
}


//获取选择的门店类型
function fnGetSelectStoreType() {
	return  $("#setStoreType").val();
}

 */

/*
 * 导出Excel文件
 */

function fnExportExcel() {

	var BrandName = "";
	var StoreCode = $("#StoreListVal").val();
	var ChannelName = "";
	var Paymentkey = $("#setPaymentkey").select2("val");
	if (Paymentkey == "ALL") {
		Paymentkey = "";
	}
	var queryText = encodeURI(encodeURI("查询日期:+" + dteTimeStart + "~"
			+ dteTimeEnd 
			+ " \n门店类型:" + $("#setStoreType").find("option:selected").text()
			+ " \n品牌范围:" + $("#setBrandName").val() + " \n门店范围:"
			+ $("#setStoreListText").val() + "\n支付方式:"
			+ $("#setPaymentkey").select2('data').text));

	var rPam = "?TimeStart=" + dteTimeStart + "&TimeEnd=" + dteTimeEnd
			+ "&BrandName=" + BrandName + "&StoreCode=" + StoreCode
			+ "&ChannelName=" + ChannelName + "&Paymentkey=" + Paymentkey
			+ "&queryText=" + queryText;
	var exportIframe = document.getElementById("exportIframe");
	exportIframe.src = baseUrl + '/merchant/queryMerchantDetailExportExcel/' + rPam;

}





/*
 * 设置门店类型值改变事件
 * */
/*
 * 
function fnSetStoreTypeChange(){
	fnInitForm();
	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);
	$("#BranCode").val("");
}
 */


var repmerchantone = function() {

	/*
	 * 
	var handleDatePickers = function() {
		if (!jQuery().daterangepicker) {
			return;
		}
		$('#defaultrange').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment().subtract('days', 7),
					endDate : moment(),
					minDate : '01/01/2012',
					maxDate : '12/31/2099',
					locale : {
						applyLabel : '确认',
						cancelLabel : '取消',
						fromLabel : '从',
						toLabel : '至',
						customRangeLabel : '自定义范围',
						daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
						monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',
								'七月', '八月', '九月', '十月', '十一月', '十二月' ],
						firstDay : 1
					}
				},
				function(start, end) {
					$('#defaultrange input').val(
							start.format('YYYY-MM-DD') + ' ~ '
									+ end.format('YYYY-MM-DD'));
					dteTimeStart = start.format('YYYY-MM-DD');
					dteTimeEnd = end.format('YYYY-MM-DD');
				});

		$('#defaultrange input').val(
				moment().subtract('days', 7).format('YYYY-MM-DD') + ' ~ '
						+ moment().format('YYYY-MM-DD'));

		dteTimeStart = moment().subtract('days', 7).format('YYYY-MM-DD');
		dteTimeEnd = moment().format('YYYY-MM-DD');

	}
	 */

	return {

		init : function() {
			//handleDatePickers();
			//fnSetStoreType();
			fnGetMenuFunction(baseUrl, funcID);
			fnListeningActQuery();
			//fnInitForm();
			//fnListeningCtrSelectChange();

			fnQueryData("", "", "", "");
//			$("#setBrandName").attr("disabled", "disabled");
//			$("#setStoreListText").attr("disabled", "disabled");
//			$("#setBrandName").val("全部品牌");
//			$("#setStoreListText").val("全部门店");

//			initdteTimeStart = dteTimeStart;
//			initdteTimeEnd = dteTimeEnd;
		}
	};

}();
