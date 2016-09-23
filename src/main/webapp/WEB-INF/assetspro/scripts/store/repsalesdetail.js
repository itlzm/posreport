var baseUrl = $("#baseUrl").val();
var funcID = "R0109";
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

	var BrandName = "";
	var StoreName = "";
	var ChannelName = "";

	fnQueryData(BrandName, StoreName, ChannelName);

}

// 查询数据
function fnQueryData(BrandCode, StoreCode, ChannelCode) {
	fnLoadingShowAndClose(true);
	StoreCode = $("#StoreListVal").val();
	BrandCode = $("#BranCode").val();
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/store/queryRepSalesDetail/',
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd,
			BrandCode : BrandCode,
			StoreCode : StoreCode,
			ChannelCode : ChannelCode,
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

			fnListTableData(listData, pageData, listDataSum);
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}


function fnOrderDetailHide(){
	$('#OrderDetailDiv').modal('hide');
}

function fnOrderDetail(orderCode) {

	fnLoadingShowAndClose(true);
	var StoreCode = $("#StoreListVal").val();
	var BrandCode = $("#BranCode").val();
	var ChannelCode ="";
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/store/queryRepSalesDetailOrderDetail/',
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd,
			BrandCode : BrandCode,
			StoreCode : StoreCode,
			ChannelCode : ChannelCode,
			PageIndex : pageIndex,
			PageSize : pageSzie,
			OrderCode : orderCode
		},
		success : function(data) {

			//订单表头
			var listDataHeader = data.listDataHeader;
			
			// 订单明细
			var listDataItem = data.listDataItem;
			var listDataItemSum = data.listDataItemSum;
			
			// 订单折扣优惠
			var listDataFavourable = data.listDataFavourable;
			var listDataFavourableSum = data.listDataFavourableSum;

			// 订单支付明细
			var listDataPayment = data.listDataPayment;
			var listDataPaymentSum = data.listDataPaymentSum;
			
			//绑定订单头部
			$("#edtbrandCodeName").val(listDataHeader[0].brandCodeName);
			$("#edtstoreCodeName").val(listDataHeader[0].storeCodeName);
			$("#edtstoreTypeName").val(listDataHeader[0].storeTypeName);
			$("#edtchannelName").val(listDataHeader[0].channelName);
			$("#edtbusinessDate").val(listDataHeader[0].businessDate);
			$("#edtorderNo").val(listDataHeader[0].orderNo);
			$("#edtdescription").val(listDataHeader[0].description);
			$("#edtthirdOrderNo").val(listDataHeader[0].thirdOrderNo);
			$("#edtorderPeopleNum").val(listDataHeader[0].orderPeopleNum);
			
			$("#edtorderTotalAmount").val(listDataHeader[0].orderTotalAmount);
			$("#edtDiscountAmount").val(listDataHeader[0].DiscountAmount);
			$("#edtnetAmountPrice").val(listDataHeader[0].netAmountPrice);
			$("#edtorderDate").val(listDataHeader[0].orderDate);
			
			$("#edtcommissionAmount").val(listDataHeader[0].commissionAmount);
			$("#edtbusinessFavourableAmount").val(listDataHeader[0].businessFavourableAmount);
			$("#edtsubNetAmount").val(listDataHeader[0].subNetAmount);
			$("#edtplatformFavourableAmount").val(listDataHeader[0].platformFavourableAmount);
			
			$("#edtinvoiceHeader").val(listDataHeader[0].invoiceHeader);
			$("#edtorderTypeDesc").val(listDataHeader[0].orderTypeDesc);
			$("#edtorderStatusDesc").val(listDataHeader[0].orderStatusDesc);
			
			$("#edtemployName").val(listDataHeader[0].employName);
			
			var strHtml = "";
			strHtml+="<thead>";
			strHtml+="<tr>";
			strHtml+="<th>";
			strHtml+="名称";
			strHtml+="</th>";
			strHtml+="<th>";
			strHtml+="数量";
			strHtml+="</th>";
			
			strHtml+="<th>";
			strHtml+="金额";
			strHtml+="</th>";
			strHtml+="</tr>";
			strHtml+="</thead>";
			for(var i=0;i<listDataItem.length;i++){
				strHtml+="<tr>";
				strHtml+="<td>";
				strHtml+=listDataItem[i].productName;
				strHtml+="</td>";
				strHtml+="<td align='right'>";
				strHtml+=listDataItem[i].numDesc;
				strHtml+="</td>";
				strHtml+="<td align='right'>";
				strHtml+=listDataItem[i].netAmountPrice;
				strHtml+="</td>";
				strHtml+="<tr>";
			}
			
			
			document.getElementById('DataDetail').innerHTML = "";
			$('#DataDetail').append(strHtml);
			
			strHtml="";
			strHtml+="<thead>";
			strHtml+="<tr>";
			strHtml+="<th>";
			strHtml+="折扣优惠名称";
			strHtml+="</th>";
			strHtml+="<th>";
			strHtml+="优惠金额";
			strHtml+="</th>";
		
			strHtml+="</tr>";
			strHtml+="</thead>";
			for(var i=0;i<listDataFavourable.length;i++){
				strHtml+="<tr>";
				strHtml+="<td>";
				strHtml+=listDataFavourable[i].favourableName;
				strHtml+="</td>";
				strHtml+="<td align='right'>";
				strHtml+=listDataFavourable[i].displayAmount;
				strHtml+="</td>";
				strHtml+="<tr>";
			}
			for(var i=0;i<listDataFavourableSum.length;i++){
				strHtml+="<tr>";
				strHtml+="<td>";
				strHtml+=listDataFavourableSum[i].favourableName;
				strHtml+="</td>";
				strHtml+="<td align='right'>";
				strHtml+=listDataFavourableSum[i].displayAmount;
				strHtml+="</td>";
				strHtml+="<tr>";
			}
			
			document.getElementById('DataFavourable').innerHTML = "";
			$('#DataFavourable').append(strHtml);
			
			
			
			strHtml="";
			strHtml+="<thead>";
			strHtml+="<tr>";
			strHtml+="<th>";
			strHtml+="支付名称";
			strHtml+="</th>";
			strHtml+="<th>";
			strHtml+="支付金额";
			strHtml+="</th>";
		
			strHtml+="</tr>";
			strHtml+="</thead>";
			for(var i=0;i<listDataPayment.length;i++){
				strHtml+="<tr>";
				strHtml+="<td>";
				strHtml+=listDataPayment[i].paymentKeyName;
				strHtml+="</td>";
				strHtml+="<td align='right'>";
				strHtml+=listDataPayment[i].displayAmount;
				strHtml+="</td>";
				strHtml+="<tr>";
			}
			for(var i=0;i<listDataPaymentSum.length;i++){
				strHtml+="<tr>";
				strHtml+="<td>";
				strHtml+=listDataPaymentSum[i].paymentKeyName;
				strHtml+="</td>";
				strHtml+="<td align='right'>";
				strHtml+=listDataPaymentSum[i].displayAmount;
				strHtml+="</td>";
				strHtml+="<tr>";
			}
			
			document.getElementById('DataPayment').innerHTML = "";
			$('#DataPayment').append(strHtml);
			
			
			$('#OrderDetailDiv').modal('show');
			
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}

function fnListTableData(listData, pageData, listDataSum) {
	// 绑定明细列表信息
	var len = listDataSum.length;
	var strHtml = "";
	var strColName = "";
	var strColNameValue = "";
	for (var i = 0; i < len; i++) {
		if (i == 0) {
			strColName += "<thead><tr>";
			strColName += "<th scope='col' style=\"text-align: center;\">行号 </th>";
			for (obj in listDataSum[i]) {

				if (obj == "营业日期") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "门店" || obj == "渠道" || obj == "品牌"
						|| obj == "订单时间") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 80px;padding-right: 80px;'>"
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
		$
				.each(
						listData[i],
						function(key, value) {
							if (isfloat(value)) {

								strColNameValue += "<td align='right'>" + value
										+ "</td>";
							} else {
								if (key == "订单单号") {

									if (value != "") {
										var valueAry = value.split("|=|");

										strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnOrderDetail('"
												+ valueAry[0]
												+ "')\">"
												+ valueAry[1]
												+ "</a></td>";

									} else {
										strColNameValue += "<td>" + value
												+ "</td>";
									}
								} else {
									strColNameValue += "<td>" + value + "</td>";
								}
							}
						});
		strColNameValue += "</tr>";
	}

	for (var i = 0; i < listDataSum.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td></td>"; // 明细行号列
		$.each(listDataSum[i], function(key, value) {
			if (isfloat(value)) {

				strColNameValue += "<td align='right'>" + value + "</td>";
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

		fnQueryData(BrandName, StoreName, ChannelName);
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {
	/*
	 * dteTimeStart = ""; // 时间开始时间 dteTimeEnd = ""; // 时间结束时间
	 */

	dteTimeStart = initdteTimeStart;
	dteTimeEnd = initdteTimeEnd;

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
					monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月',
							'八月', '九月', '十月', '十一月', '十二月' ],
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

	$("#setStoreType").val("ALL");

	fnInitForm();

	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);
	$("#BranCode").val("");
}



var aryStoreList = "";
function fnInitForm() {

	var strStoreType = $("#setStoreType").val();

	// 获取授权的所有门店信息
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/brand/getStoreList?StoreType='
				+ strStoreType,
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

function fnGetQueryStoreTree(strBranCode, strBranName, treeText,
		strStoreTreeText, treeVal) {
	$("#BranCode").val(strBranCode);
	$("#setBrandName").val(strBranName);
	$("#setStoreListText").val(strStoreTreeText);
	$("#StoreListVal").val(treeVal);
}

function fnGetSelectStoreTree() {
	return $("#StoreListVal").val();
}

// 获取选择的门店类型
function fnGetSelectStoreType() {
	return $("#setStoreType").val();
}

/*
 * 导出Excel文件
 */
function fnExportExcel() {

	var BrandCode =  $("#BranCode").val();
	var StoreCode = $("#StoreListVal").val();
	var ChannelName = "";

	var queryText = encodeURI(encodeURI("查询日期:+" + dteTimeStart + "~"
			+ dteTimeEnd + " \n门店类型:"
			+ $("#setStoreType").find("option:selected").text() + " \n品牌范围:"
			+ $("#setBrandName").val() + " \n门店范围:"
			+ $("#setStoreListText").val()));

	var rPam = "?TimeStart=" + dteTimeStart + "&TimeEnd=" + dteTimeEnd
			+ "&BrandCode=" + BrandCode + "&StoreCode=" + StoreCode
			+ "&ChannelCode=" + ChannelName + "&queryText=" + queryText;
	var exportIframe = document.getElementById("exportIframe");
	exportIframe.src = baseUrl + '/store/queryRepSalesDetailOrderDetailExportExcel/' + rPam;

}

/*
 * 设置门店类型值改变事件
 */
function fnSetStoreTypeChange() {
	fnInitForm();
	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);
	$("#BranCode").val("");
}

var repsalesdetail = function() {

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

	return {

		init : function() {
			handleDatePickers();
			fnSetStoreType();
			fnGetMenuFunction(baseUrl, funcID);
			fnListeningActQuery();
			fnInitForm();

			fnQueryData("", "", "");
			$("#setBrandName").attr("disabled", "disabled");
			$("#setStoreListText").attr("disabled", "disabled");
			$("#setBrandName").val("全部品牌");
			$("#setStoreListText").val("全部门店");

			initdteTimeStart = dteTimeStart;
			initdteTimeEnd = dteTimeEnd;
		}
	};

}();
