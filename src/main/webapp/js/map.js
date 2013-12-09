$(document).ready(function() {
	var mapArea = $("map area");
	var hmc = [];
	var smTimer;
	var event;

	mapArea.hover(onArea, outArea).mousemove(onMoveArea);

	function onArea() {
		var id = $(this).attr("id");
		showMoreCities(id);
	}

	function outArea() {
		var id = $(this).attr("id");
		hideMoreCities(id);
	}

	function onMoveArea(e) {
		event = e;
		var id = $(this).attr("id");
		$("div." + id + "").show();
	}

	function showMoreCities(id) {
		var parent = $("div." + id + "").show();
		var elem = $("#more-cities-" + id);
		elem.addClass("popup-info ui-corner-all ui-widget-content ui-dialog");
		elem.unbind("hover").hover(function() {
			window.clearTimeout(hmc[id]);
		}, function() {
			hideMoreCities(id);
		});

		window.clearTimeout(smTimer);
		smTimer = window.setTimeout(function() {
			var windowHeight = $(window).height();
			var scrollTop = $(window).scrollTop();
			var posX = event.clientX - 70;
			var posY = parent.offset().top;
			var elWidth = elem.width();
			var elHeight = elem.height();
			var top = posY - 16;
			if (windowHeight < (top - scrollTop) + elHeight + 30)
				top = windowHeight + scrollTop - elHeight - 30;
			var left = posX - elWidth + 20;

			elem.css("left", left);
			elem.css("top", top);
			elem.fadeIn(400);
		}, 500);
	}

	function hideMoreCities(id) {
		window.clearTimeout(smTimer);
		hmc[id] = window.setTimeout(function() {
			$("#more-cities-" + id).fadeOut(100);
			$("div." + id + "").hide();
		}, 200);
	}
});