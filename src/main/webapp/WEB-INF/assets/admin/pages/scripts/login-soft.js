var baseUrl = $("#baseUrl").val();

var Login = function() {

	var handleLogin = function() {
		$('.login-form')
				.validate(
						{
							errorElement : 'span', // default input error
													// message container
							errorClass : 'help-block', // default input error
														// message class
							focusInvalid : false, // do not focus the last
													// invalid input
							rules : {
								username : {
									required : true
								},
								password : {
									required : true
								},
								remember : {
									required : false
								}

							},

							messages : {
								username : {
									required : "用户名不能空"
								},
								password : {
									required : "用户密码不能空."
								}
							},

							invalidHandler : function(event, validator) { // display
																			// error
																			// alert
																			// on
																			// form
																			// submit
								$('.alert-danger', $('.login-form')).show();
							},

							highlight : function(element) { // hightlight error
															// inputs
								$(element).closest('.form-group').addClass(
										'has-error'); // set error class to
														// the control group
							},

							success : function(label) {
								label.closest('.form-group').removeClass(
										'has-error');
								label.remove();
							},

							errorPlacement : function(error, element) {
								error.insertAfter(element
										.closest('.input-icon'));
							},

							submitHandler : function(form) {
								// form.submit();
								var getLoginUserName = document
										.getElementById("username").value;
								var getLoginUserPassword = document
										.getElementById("password").value;
								var getCompanyCode = $("#companyCode").select2(
										"val");

								if (getCompanyCode == "") {
									alert("请选择商户!");
									return;
								}

								$.ajax({
									type : "POST",
									dataType : "json",
									// cache:true,
									url : baseUrl + "/handleLogin",
									data : {
										username : getLoginUserName,
										password : getLoginUserPassword,
										companycode : getCompanyCode
									},
									success : function(data) {
										if (data.length > 0) {
											window.location.href = baseUrl
													+ "/store/reppaymode";
										} else {
											alert("用户不存在,请重试");
										}
									},
									error : function(e) {

									}
								});
							}
						});

		$('.login-form input').keypress(function(e) {
			if (e.which == 13) {
				if ($('.login-form').validate().form()) {
					$('.login-form').submit();
				}
				return false;
			}
		});
	}

	var handleForgetPassword = function() {
		$('.forget-form').validate({
			errorElement : 'span', // default input error message container
			errorClass : 'help-block', // default input error message class
			focusInvalid : false, // do not focus the last invalid input
			ignore : "",
			rules : {
				email : {
					required : true,
					email : true
				}
			},

			messages : {
				email : {
					required : "Email is required."
				}
			},

			invalidHandler : function(event, validator) { // display error
															// alert on form
															// submit

			},

			highlight : function(element) { // hightlight error inputs
				$(element).closest('.form-group').addClass('has-error'); // set
																			// error
																			// class
																			// to
																			// the
																			// control
																			// group
			},

			success : function(label) {
				label.closest('.form-group').removeClass('has-error');
				label.remove();
			},

			errorPlacement : function(error, element) {
				error.insertAfter(element.closest('.input-icon'));
			},

			submitHandler : function(form) {
				form.submit();
			}
		});

		$('.forget-form input').keypress(function(e) {
			if (e.which == 13) {
				if ($('.forget-form').validate().form()) {
					$('.forget-form').submit();
				}
				return false;
			}
		});

		jQuery('#forget-password').click(function() {
			jQuery('.login-form').hide();
			jQuery('.forget-form').show();
		});

		jQuery('#back-btn').click(function() {
			jQuery('.login-form').show();
			jQuery('.forget-form').hide();
		});

	}

	var handleRegister = function() {

		function format(state) {
			if (!state.id)
				return state.text; // optgroup
			return "<img class='flag' src='../../assets/global/img/flags/"
					+ state.id.toLowerCase() + ".png'/>&nbsp;&nbsp;"
					+ state.text;
		}

		$("#select2_sample4")
				.select2(
						{
							placeholder : '<i class="fa fa-map-marker"></i>&nbsp;Select a Country',
							allowClear : true,
							formatResult : format,
							formatSelection : format,
							escapeMarkup : function(m) {
								return m;
							}
						});

		$('#select2_sample4').change(function() {
			$('.register-form').validate().element($(this)); // revalidate
																// the chosen
																// dropdown
																// value and
																// show error or
																// success
																// message for
																// the input
		});

		$('.register-form').validate({
			errorElement : 'span', // default input error message container
			errorClass : 'help-block', // default input error message class
			focusInvalid : false, // do not focus the last invalid input
			ignore : "",
			rules : {

				fullname : {
					required : true
				},
				email : {
					required : true,
					email : true
				},
				address : {
					required : true
				},
				city : {
					required : true
				},
				country : {
					required : true
				},

				username : {
					required : true
				},
				password : {
					required : true
				},
				rpassword : {
					equalTo : "#register_password"
				},

				tnc : {
					required : true
				}
			},

			messages : { // custom messages for radio buttons and checkboxes
				tnc : {
					required : "Please accept TNC first."
				}
			},

			invalidHandler : function(event, validator) { // display error
															// alert on form
															// submit

			},

			highlight : function(element) { // hightlight error inputs
				$(element).closest('.form-group').addClass('has-error'); // set
																			// error
																			// class
																			// to
																			// the
																			// control
																			// group
			},

			success : function(label) {
				label.closest('.form-group').removeClass('has-error');
				label.remove();
			},

			errorPlacement : function(error, element) {
				if (element.attr("name") == "tnc") { // insert checkbox
														// errors after the
														// container
					error.insertAfter($('#register_tnc_error'));
				} else if (element.closest('.input-icon').size() === 1) {
					error.insertAfter(element.closest('.input-icon'));
				} else {
					error.insertAfter(element);
				}
			},

			submitHandler : function(form) {
				form.submit();
			}
		});

		$('.register-form input').keypress(function(e) {
			if (e.which == 13) {
				if ($('.register-form').validate().form()) {
					$('.register-form').submit();
				}
				return false;
			}
		});

		jQuery('#register-btn').click(function() {
			jQuery('.login-form').hide();
			jQuery('.register-form').show();
		});

		jQuery('#register-back-btn').click(function() {
			jQuery('.login-form').show();
			jQuery('.register-form').hide();
		});
	}

	var initCompanyCode = function() {

		$.ajax({
			type : "POST",
			dataType : "json",
			// cache:true,
			url : baseUrl + "/common/dict/companycode",
			success : function(data) {

				var strcompanyCode = JSON.stringify(eval(data.companyCode));
				var strcompanyCodeTemp = "";
				if (typeof (strcompanyCode) != "undefined") {
					strcompanyCode = strcompanyCode.substring(1,
							strcompanyCode.length);
					strcompanyCodeTemp = '[{"id":"","text":"选择商户"},'
							+ strcompanyCode;
				} else {
					strcompanyCodeTemp = '[{"id":"","text":"选择商户"}]';
				}

				$("#companyCode").select2("val", "");
				$("#companyCode").select2({
					data : eval(strcompanyCodeTemp)
				});
				
				
				$("#companyCode").select2("val", "07f7a642-f2f6-4636-9c91-3e4fe9e926a0");

			},
			error : function(e) {

			}
		});
	}

	return {
		// main function to initiate the module
		init : function() {

			handleLogin();
			handleForgetPassword();
			handleRegister();
			initCompanyCode();

			$.backstretch([ baseUrl + "/assets/admin/pages/media/bg/1.jpg",
					baseUrl + "/assets/admin/pages/media/bg/2.jpg",
					baseUrl + "/assets/admin/pages/media/bg/3.jpg",
					baseUrl + "/assets/admin/pages/media/bg/4.jpg" ], {
				fade : 1000,
				duration : 8000
			});
		}

	};

}();