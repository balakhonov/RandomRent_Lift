$(function() {
	var fileUploader = $('#file-uploader');
	var progress = $('#progress');
	var progressBar = $('#progress-bar');
	var imagesList = $("#images-list").sortable().disableSelection();
	var addPhtoButton = $("#add-photo-button").button();
	addPhtoButton.on("click", function() {
		$('#file-uploader').click();
		return false;
	});

	fileUploader.fileupload({
		dataType : 'json',
		add : function(e, data) {
			progressBar.css('width', '0%');
			progress.show();
			data.submit();
		},
		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10) + '%';
			progressBar.css('width', progress);
		},
		done : function(e, data) {
			$.each(data.result, function(index, image) {
				imagesList.append(creatThumbImgWrapper(image));
			});
			// $.each(data.files, function(index, file) {
			// console.log(file.name);
			// $('<p/>').text(file.name).appendTo(document.body);
			// });
			progress.fadeOut();
		}
	});

	function creatThumbImgWrapper(image) {
		var img = $("<img/>");
		img.load(loadedListener).hide().attr('src', '/ap-images/' + image.name + '-thumb-150x150');

		var overlay = $("<div/>");
		overlay.addClass("ov-" + image.name);
		overlay.append('<img src="/images/image-loader.gif" />');

		var thumbDiv = $("<div/>");
		thumbDiv.addClass("thumb-img");
		thumbDiv.append(overlay).append(img);

		var li = $("<li></li>");
		li.addClass("thumb-img-wrap");
		li.append(thumbDiv);

		function loadedListener() {
			overlay.detach();
			img.fadeIn(200);
		}

		return li;
	}

	$(".add-apartment-filter a").click(function() {
		$(".add-apartment-filter a").removeClass("selected");
		$(this).addClass("selected");
		$(".add-apartment-filter2").hide();
		$(".add-apartment-filter3").hide();
		$(".add-apartment-filter4").hide();
	});
	$(".add-apartment-filter .rent").click(function() {
		$(".add-apartment-filter2").show();
	});
	$(".add-apartment-filter .buy-sell").click(function() {
		$(".add-apartment-filter4").show();
	});

	$(".add-apartment-filter2 a").click(function() {
		$(".add-apartment-filter2 a").removeClass("selected");
		$(this).addClass("selected");
	});
	$(".add-apartment-filter2").click(function() {
		$(".add-apartment-filter3").show();
	});
});