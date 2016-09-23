var baseUrl = $("#baseUrl").val();
var funcID = "R0305";
var pageIndex = 1;
var pageSzie = 15;

var initdteTimeStart;
var initdteTimeEnd;

var initdteTBTimeStart;
var initdteTBTimeEnd;

// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间

var dteTBTimeStart; // 时间开始时间
var dteTBTimeEnd; // 时间结束时间

var myChart1;
var myChart2;
var myChart3;

var option1;

// 查询按钮注册
function fnListeningActQuery() {
	$(document).ready(function() {
		$('#ActQuery').click(function() {
			// 开启编辑控件可用
			fnBtnClickShowQuery();
		});
	});
}

// 显示查询窗口
function fnBtnClickShowQuery() {

	if ($("#QueryDiv").css("display") == 'none') {
		$("#QueryDiv").css("display", "inline");
	} else {
		$("#QueryDiv").css("display", "none");
	}

}

// 执行查询并关闭查询窗口
function fnBtnClickSetQuery() {
	$("#QueryDiv").css("display", "none");
	pageIndex = 1;

	var BrandName = "";
	var StoreName = "";
	var ChannelName = "";
	var favourableName = "";
	var CategoryCode = "";
	fnQueryData(BrandName, StoreName, ChannelName, favourableName, CategoryCode);

}

var listDataSum;

// 查询数据
function fnQueryData(BrandCode, StoreCode, ChannelName, favourableName,
		CategoryCode) {

	// BrandCode = $("#BranCode").val();
	StoreCode = $("#StoreListVal").val();
	CategoryCode = $("#CategoryListVal").val();

	// 产品分类不按照门店查询时,CategoryCode 控制中同步存储品牌集合,所以品牌从该值中获取

	if ($("#CategoryCode").val() == "ALL" || $("#CategoryCode").val() == "") {
		BrandCode = $("#BranCode").val();
	} else {
		BrandCode = $("#CategoryCode").val();

	}

	navAry = Array();
	navAry[0] = "ALLClick$查询结果$";

	var strHtml = "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick(0)\">"
			+ "查询结果" + "</a>>>";
	document.getElementById('DataNav').innerHTML = "";
	$('#DataNav').append(strHtml);

	fnLoadingShowAndClose(true);
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/business/queryRepProductCatTBBrand/',
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd,
			BrandCode : BrandCode,
			BrandName : BrandCode,
			StoreCode : StoreCode,
			ChannelName : ChannelName,
			PageIndex : pageIndex,
			PageSize : pageSzie,
			favourableName : favourableName,
			TBTimeStart : dteTBTimeStart,
			TBTimeEnd : dteTBTimeEnd,
			CategoryCode : CategoryCode
		},
		success : function(data) {
			/*
			 * // 支付方式结构分析 var storePay = data.storePay; var storePayType =
			 * data.storePayType; fnPayModeData(storePay, storePayType);
			 * fnPayModeSubSamountData(storePay, storePayType);
			 */

			// 明细数据信息
			var listData = data.listData;
			// 分页信息;
			var pageData = data.pageData;

			// 合计行
			var listDataSumTemp = data.listDataSum;

			if (pageIndex == 1) {
				listDataSum = listDataSumTemp;
			}
			fnListTableData(listData, pageData, listDataSum);
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

				if (obj == "日期") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "品牌" || obj == "分类" || obj == "产品名称") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 80px;padding-right: 80px;'>"
							+ obj + "</th>";
				} else if (obj == "省份" || obj == "城市" || obj == "区县"
						|| obj == "所属品牌") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "所属分类") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 20px;padding-right: 20px;'>"
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

								if (key == "品牌") {

									if (value != "") {
										var valueAry = value.split("|=|");

										strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtBrandClick('"
												+ value
												+ "')\">"
												+ valueAry[1]
												+ "</a></td>";
									} else {
										strColNameValue += "<td>" + value
												+ "</td>";
									}
								} else if (key == "分类") {

									if (value != "") {
										var valueAry = value.split("|=|");

										strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtAreaClick('"
												+ value
												+ "')\">"
												+ valueAry[3]
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
				strColNameValue += "<td >" + value + "</td>";
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
		var BrandName = $("#setBrandName").select2("val");
		var StoreName = $("#setStoreName").select2("val");
		var ChannelName = $("#setChannelName").select2("val");
		var favourableName = $("#setfavourableName").select2("val");
		fnQueryData(BrandName, StoreName, ChannelName, favourableName, "");
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

var navAry = Array();

// 第一级别品牌点击
function fnEdtBrandClick(strPrm) {

	navID = 1;
	var strPrmAry = strPrm.split("|=|");

	var brandCode = strPrmAry[0];
	var brandName = strPrmAry[1];
	var areaCode = strPrmAry[2];

	var navAryL = navAry.length;
	navAry[navAryL] = "BrandClick$" + brandName + "$" + strPrm;
	var strHtml = "";
	for (var i = 0; i < navAry.length; i++) {
		var alltemp = navAry[i];
		var showName = alltemp.split("$")[1];
		strHtml += "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick("
				+ (i) + ")\">" + showName + "</a>>>";
		// }

		document.getElementById('DataNav').innerHTML = "";
		$('#DataNav').append(strHtml);
	}

	if (areaCode != "") {

		// 查询区域数据;
		fnLoadingShowAndClose(true);
		$.ajax({
			type : "POST",
			dataType : "json",
			url : baseUrl + '/business/queryRepProductCatTBBrandChildren/',
			data : {
				TimeStart : dteTimeStart,
				TimeEnd : dteTimeEnd,
				BrandCode : brandCode,
				BrandName : brandName,
				StoreCode : $("#StoreListVal").val(),
				ChannelName : "",
				PageIndex : pageIndex,
				PageSize : pageSzie,
				AreaCode : areaCode,
				TBTimeStart : dteTBTimeStart,
				TBTimeEnd : dteTBTimeEnd,
				CategoryCode : $("#CategoryListVal").val()
			},
			success : function(data) {

				// 明细数据信息
				var listData = data.listData;
				// 分页信息;
				var pageData = data.pageData;

				// 合计行
				var listDataSumTemp = data.listDataSum;

				if (pageIndex == 1) {
					listDataSum = listDataSumTemp;
				}
				fnListTableData(listData, pageData, listDataSum);
				fnLoadingShowAndClose(false);
			},
			error : function(e) {
				fnLoadingShowAndClose(false);
			}
		});
	} else {

		// 直接查询门店数据

	}
}

var mapNav = new Map();

function fnNavDataClick(index) {
	var navAryL = navAry.length;
	var navAryTemp = new Array();

	for (var i = 0; i < index; i++) {
		navAryTemp[i] = navAry[i];
	}

	var strAll = navAry[index];
	var strPrmAry = strAll.split("$");
	var stype = strPrmAry[0];
	var sName = strPrmAry[1];
	var sPrm = strPrmAry[2];

	navAry = navAryTemp;

	if (stype == "BrandClick") {
		fnEdtBrandClick(sPrm);
	} else if (stype == "AreaClick") {
		fnEdtAreaClick(sPrm);
	} else if (stype == "ALLClick") {
		fnBtnClickSetQuery();
	}

}

// 第二级别区域点击
function fnEdtAreaClick(strPrm) {

	var strPrmAry = strPrm.split("|=|");

	var brandCode = strPrmAry[0];
	var brandName = strPrmAry[1];
	var areaCode = strPrmAry[2];
	var areaName = strPrmAry[3];
	var isStore = strPrmAry[4];

	var navAryL = navAry.length;
	navAry[navAryL] = "AreaClick$" + areaName + "$" + strPrm;
	var strHtml = "";
	for (var i = 0; i < navAry.length; i++) {
		var alltemp = navAry[i];
		var showName = alltemp.split("$")[1];
		strHtml += "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick("
				+ (i) + ")\">" + showName + "</a>>>";
	}

	document.getElementById('DataNav').innerHTML = "";
	$('#DataNav').append(strHtml);

	if (areaCode != "") {

		if (isStore == "N") {

			// 查询区域数据;
			fnLoadingShowAndClose(true);
			$.ajax({
				type : "POST",
				dataType : "json",
				url : baseUrl + '/business/queryRepProductCatTBBrandChildren/',
				data : {
					TimeStart : dteTimeStart,
					TimeEnd : dteTimeEnd,
					BrandCode : brandCode,
					BrandName : brandName,
					StoreCode : $("#StoreListVal").val(),
					ChannelName : "",
					PageIndex : pageIndex,
					PageSize : pageSzie,
					AreaCode : areaCode,
					TBTimeStart : dteTBTimeStart,
					TBTimeEnd : dteTBTimeEnd,
					CategoryCode : $("#CategoryListVal").val()
				},
				success : function(data) {

					// 明细数据信息
					var listData = data.listData;
					// 分页信息;
					var pageData = data.pageData;

					// 合计行
					var listDataSumTemp = data.listDataSum;

					if (pageIndex == 1) {
						listDataSum = listDataSumTemp;
					}
					fnListTableData(listData, pageData, listDataSum);
					fnLoadingShowAndClose(false);
				},
				error : function(e) {
					fnLoadingShowAndClose(false);
				}
			});
		} else {

			// 查询区域数据;
			fnLoadingShowAndClose(true);
			$
					.ajax({
						type : "POST",
						dataType : "json",
						url : baseUrl
								+ '/business/queryRepProductCatTBBrandChildrenProduct/',
						data : {
							TimeStart : dteTimeStart,
							TimeEnd : dteTimeEnd,
							BrandCode : brandCode,
							BrandName : brandName,
							StoreCode : $("#StoreListVal").val(),
							ChannelName : "",
							PageIndex : pageIndex,
							PageSize : pageSzie,
							AreaCode : areaCode,
							TBTimeStart : dteTBTimeStart,
							TBTimeEnd : dteTBTimeEnd,
							CategoryCode : $("#CategoryListVal").val()
						},
						success : function(data) {

							// 明细数据信息
							var listData = data.listData;
							// 分页信息;
							var pageData = data.pageData;

							// 合计行
							var listDataSumTemp = data.listDataSum;

							if (pageIndex == 1) {
								listDataSum = listDataSumTemp;
							}
							fnListTableData(listData, pageData, listDataSum);
							fnLoadingShowAndClose(false);
						},
						error : function(e) {
							fnLoadingShowAndClose(false);
						}
					});
		}

	} else {

		// 直接查询门店数据

	}
}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {

	dteTimeStart = initdteTimeStart;
	dteTimeEnd = initdteTimeEnd;

	dteTBTimeStart = initdteTBTimeStart;
	dteTBTimeEnd = initdteTBTimeEnd;

	$('#defaultrange').daterangepicker(
			{
				opens : (Metronic.isRTL() ? 'left' : 'right'),
				format : 'MM/DD/YYYY',
				separator : ' to ',
				startDate : moment(),
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

	$('#defaultrangetb').daterangepicker(
			{
				opens : (Metronic.isRTL() ? 'left' : 'right'),
				format : 'MM/DD/YYYY',
				separator : ' to ',
				startDate : moment().subtract('days', 1),
				endDate : moment().subtract('days', 1),
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
				$('#defaultrangetb input').val(
						start.format('YYYY-MM-DD') + ' ~ '
								+ end.format('YYYY-MM-DD'));
				dteTBTimeStart = start.format('YYYY-MM-DD');
				dteTBTimeEnd = end.format('YYYY-MM-DD');

			});

	$('#defaultrange input').val(
			moment().format('YYYY-MM-DD') + ' ~ '
					+ moment().format('YYYY-MM-DD'));

	$('#defaultrangetb input').val(
			moment().subtract('days', 1).format('YYYY-MM-DD') + ' ~ '
					+ moment().subtract('days', 1).format('YYYY-MM-DD'));

	$("#setStoreType").val("ALL");

	fnInitForm();

	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);

	$("#setCategoryName").val("全部品牌分类");
	$("#setCategoryListText").val("全部产品分类");
	$("#CategoryListVal").val(aryCategoryList);

	$("#CategoryCode").val("ALL");
	$("#BranCode").val("ALL");

}

var aryStoreList = "";

var aryCategoryList = "";

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

	$("#BranCode").val("ALL");
	var strSelectBrandCode = "ALL";

	// 获取分类信息
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/brand/getCategoryList?SelectBrandCode='
				+ strSelectBrandCode,
		dataType : "json",
		async : false,
		success : function(data) {

			var strCategory = "";
			var categoryName = data.CategoryName;
			for (var i = 0; i < categoryName.length; i++) {

				var ISLAST = categoryName[i].ISLAST;
				var ID = categoryName[i].ID;
				if (ISLAST == "Y") {
					strCategory += ID + "|";
				}
			}

			if (strCategory != "") {
				strCategory = strCategory.substr(0, strCategory.length - 1);
			}
			$("#CategoryListVal").val(strCategory);
			aryCategoryList = strCategory;

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

function fnCategoryTree() {
	var asyncSettings = {
		type : 'iframe',
		closeable : false,
		padding : false,
		cache : false,
		url : baseUrl + '/queryCategoryList'
	};
	$('#setCategoryList').webuiPopover('destroy').webuiPopover(
			$.extend({}, asyncSettings));
}

fnCategoryTree();

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

function fnGetSelectCategoryTree() {
	return $("#CategoryListVal").val();
}

function fnGetQueryCategoryTree(strCategoryCode, strCategoryName, treeText,
		strCategoryTreeText, treeVal) {

	$("#CategoryCode").val(strCategoryCode);
	$("#setCategoryName").val(strCategoryName);
	$("#setCategoryListText").val(strCategoryTreeText);
	$("#CategoryListVal").val(treeVal);

}

/*
 * 获取选择的门店类型
 */
function fnGetSelectStoreType() {
	return $("#setStoreType").val();
}

/*
 * 获取已经选择的品牌
 */
function fnGetSelectBrandCode() {
	return $("#BranCode").val();
}

/*
 * 导出Excel文件
 */
function fnExportExcel() {
	var BrandCode = "";

	// 产品分类不按照门店查询时,CategoryCode 控制中同步存储品牌集合,所以品牌从该值中获取

	if ($("#CategoryCode").val() == "ALL" || $("#CategoryCode").val() == "") {
		BrandCode = $("#BranCode").val();
	} else {
		BrandCode = $("#CategoryCode").val();

	}

	var StoreCode = $("#StoreListVal").val();
	var CategoryCode = $("#CategoryListVal").val();
	var ChannelName = "";
	var queryText = encodeURI(encodeURI("查询日期:+" + dteTimeStart + "~"
			+ dteTimeEnd + " \n门店类型:"
			+ $("#setStoreType").find("option:selected").text() + " \n品牌范围:"
			+ $("#setBrandName").val() + " \n门店范围:"
			+ $("#setStoreListText").val() + " \n品牌分类范围:"
			+ $("#setCategoryName").val() + " \n分类范围:"
			+ $("#setCategoryListText").val()));

	var rPam = "?TimeStart=" + dteTimeStart + "&TimeEnd=" + dteTimeEnd
			+ "&BrandCode=" + BrandCode + "&StoreCode=" + StoreCode
			+ "&CategoryCode=" + CategoryCode + "&ChannelName=" + ChannelName
			+ "&TBTimeStart=" + dteTBTimeStart + "&TBTimeEnd=" + dteTBTimeEnd
			+ "&queryText=" + queryText;
	var exportIframe = document.getElementById("exportIframe");
	exportIframe.src = baseUrl
			+ '/business/queryRepProductCatTBBrandChildrenProductExportExcel/'
			+ rPam;
}

/*
 * 设置门店类型值改变事件
 */
function fnSetStoreTypeChange() {
	fnInitForm();
	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);

	$("#setCategoryName").val("全部品牌分类");
	$("#setCategoryListText").val("全部产品分类");
	$("#CategoryListVal").val(aryCategoryList);

	$("#CategoryCode").val("ALL");
	$("#BranCode").val("ALL");
}

var repproductcattb = function() {

	var handleDatePickers = function() {
		if (!jQuery().daterangepicker) {
			return;
		}
		$('#defaultrange').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment(),
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

		$('#defaultrangetb').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment().subtract('days', 1),
					endDate : moment().subtract('days', 1),
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
					$('#defaultrangetb input').val(
							start.format('YYYY-MM-DD') + ' ~ '
									+ end.format('YYYY-MM-DD'));
					dteTBTimeStart = start.format('YYYY-MM-DD');
					dteTBTimeEnd = end.format('YYYY-MM-DD');

				});

		$('#defaultrange input').val(
				moment().format('YYYY-MM-DD') + ' ~ '
						+ moment().format('YYYY-MM-DD'));

		$('#defaultrangetb input').val(
				moment().subtract('days', 1).format('YYYY-MM-DD') + ' ~ '
						+ moment().subtract('days', 1).format('YYYY-MM-DD'));

		dteTimeStart = moment().format('YYYY-MM-DD');
		dteTimeEnd = moment().format('YYYY-MM-DD');

		dteTBTimeStart = moment().subtract('days', 1).format('YYYY-MM-DD');
		dteTBTimeEnd = moment().subtract('days', 1).format('YYYY-MM-DD');

	}

	return {

		init : function() {
			handleDatePickers();
			fnSetStoreType();
			fnGetMenuFunction(baseUrl, funcID);
			fnListeningActQuery();
			fnInitForm();

			setTimeout(function() {
				fnQueryData("", "", "", "", "");
			}, 1000);

			$("#setBrandName").attr("disabled", "disabled");
			$("#setStoreListText").attr("disabled", "disabled");
			$("#setBrandName").val("全部品牌");
			$("#setStoreListText").val("全部门店");

			$("#setCategoryName").attr("disabled", "disabled");
			$("#setCategoryListText").attr("disabled", "disabled");
			$("#setCategoryName").val("全部品牌分类");
			$("#setCategoryListText").val("全部产品分类");

			initdteTimeStart = dteTimeStart;
			initdteTimeEnd = dteTimeEnd;

			initdteTBTimeStart = dteTBTimeStart;
			initdteTBTimeEnd = dteTBTimeEnd;

		}
	};

}();
