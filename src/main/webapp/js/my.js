$(document).ready(function() {
	var signInDialog = $("#sign-in-dialog");
	var signUpDialog = $("#sign-up-dialog");
	var signInButton = $("#sign-in-button");
	var signUpButton = $("#sign-up-button");

	initPriceSlider();

	// Addition filters
	$("#addition-filter-fs-bt").button({
		icons : {
			primary : "ui-icon-circle-triangle-w"
		}
	}).showFilterPanel($("#addition-filter-fs"), {}, function() {
		// console.log(urlParams);
	});

	// Images Gallery
	$("#main-image").on("click", function() {
		$('#images-list').mbGallery({
			showDescription : false
		});
	});

	// $.url().param('prt', "3000");
	// console.log($.url().param(), $.url().attr('source'));

	signInDialog.dialog({
		autoOpen : false,
		modal : true,
		width : 325
	});
	signUpDialog.dialog({
		autoOpen : false,
		modal : true,
		width : 325
	});

	signInButton.click(function() {
		signInDialog.dialog("open");
	});
	signUpButton.click(function() {
		signUpDialog.dialog("open");
	});
});

function initPriceSlider() {
	var MIN_PRICE_VALUE = 0;
	var MAX_PRICE_VALUE = 8000;

	var priceSlide = $("#price-slider");
	var priceAmount = $("#price-amount");
	priceSlide.slider({
		range : true,
		min : MIN_PRICE_VALUE,
		max : MAX_PRICE_VALUE,
		values : [ $("#price-from").val(), $("#price-to").val() ],
		slide : function(event, ui) {
			priceAmount.val("$" + ui.values[0] + " - $" + ui.values[1]);
			$("#price-from").val(ui.values[0]);
			$("#price-to").val(ui.values[1]);
		}
	});
	priceAmount.val("$" + priceSlide.slider("values", 0) + " - $" + priceSlide.slider("values", 1));
}

$.fn.extend({
	showFilterPanel : function(content, options, callback) {
		var that = this;
		var lock = true;
		var elem = $('<div class="filter-panel"></div>').hide();
		elem.addClass("popup-info ui-corner-all ui-widget-content ui-dialog");
		elem.mouseover(function() {
			lock = true;
		}).mouseout(function() {
			lock = false;
		});

		var cancelButton = $("<button>Отменить</button>").button();
		var okButton = $("<button>Ок</button>").button();
		var buttons = $("<div class='filter-bottom-bts'></div>");
		buttons.append(cancelButton).append(okButton);

		cancelButton.click(hide);
		okButton.click(function() {
			hide();
			callback();
		});

		$(this).click(function(e, el) {
			lock = null;

			elem.append(content.show());
			$("body").append(elem);

			var windowHeight = $(window).height();
			var scrollTop = $(window).scrollTop();
			var posX = $(this).offset().left;
			var posY = $(this).offset().top;
			var elWidth = elem.width();
			var elHeight = elem.height();
			var top = posY - 16;
			if (windowHeight < (top - scrollTop) + elHeight)
				top = windowHeight + scrollTop - elHeight - 10;
			var left = (posX - 0) - (elWidth - 0) - 20;

			elem.css("left", left);
			elem.css("top", top);
			elem.show();
		});

		$(document).click(function(e) {
			if (lock == null) {
				lock = false;
				return;
			}

			if (lock !== true) {
				hide();
				lock = true;
			}
		});

		function hide() {
			elem.detach();
		}
		return elem;
	}
});
