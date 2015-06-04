$(document).ready(function() {

	// Drop Nav of Subscription
	$('#mySubscription').click(function() {
		$('.dropNav').fadeToggle('100');
	});

	// Drop Menu of Check List
	$('#labels').click(function() {
		$('.checklistContainer').fadeToggle('100');
	});

	// Import Card Area
	$('#importCard').click(function() {
		$('.importCardArea').fadeIn('100');
	});

	// Click outside Import Card Area
	$('.importCardArea').click(function() {
		$('.importCardArea').fadeOut('100');
	});
	$(".importCardBox").click(function(e) {
		e.stopPropagation();
	});

	// Settings toggling
	$(".settingsTitle").on("click", function(event) {
		// if ($(this).hasClass("title")) {

		$(".settingsTitle").removeClass("settingsTitleBox");
		$('.settingsFormArea').hide();

		var linkData = $(event.target).data('cont');
		$('.' + linkData).fadeIn();

		$(event.target).addClass('settingsTitleBox');
		console.log("clicked");
		// }
	});

	// Switch Between List view and Card View
	$('#listView').click(function() {
		$('.tableCards').fadeOut('fast');
		$('.tableList').fadeIn('fast');
		$('.tableControls').removeClass('tableControls-clicked');
		$('#listView').addClass('tableControls-clicked');
		console.log("clicked");

	});

	$('#cardView').click(function() {
		$('.tableList').fadeOut('fast');
		$('.tableCards').fadeIn('fast');
		$('.tableControls').removeClass('tableControls-clicked');
		$('#cardView').addClass('tableControls-clicked');
		console.log("clicked");
	});

	// Highlighting Table Pages Button
	$('div.tablePagesBtn').click(function() {
		$('div.tablePagesBtn').removeClass('tableControls-clicked');
		$(this).addClass('tableControls-clicked');
	});
	$('a.tablePagesBtn').click(function() {
		$('div.tablePagesBtn').removeClass('tableControls-clicked');
	});

	// Subscription option in filter menu
	$('.subsCheckBox').click(function() {
		$(this).toggleClass('subsCheckBox-clicked');
	});

	// Give style to checked card
	// $('input:checkbox').click(function () {
	$('.checkCard .card-check-box').click(function() {

		// $('#filterForm:cars:1:j_idt94_input').click(function () {

		if ($(this).is(':checked')) {
			$(this).parent().parent().addClass('cardBoxChecked');
		} else {
			$(this).parent().parent().removeClass('cardBoxChecked');
		}
	});

	// Sliding Down Filter Area
	$('.edSearchAdvanced').click(function() {
		$('.filterArea').toggleClass('open');
		if ($('.filterArea') == "")
			alert();
	});

	// Sliding Down Filter Area
	$('#closeAdvancedSearch').click(function() {
		$('.filterArea').toggleClass('open');
		if ($('.filterArea') == "")
			alert();
	});

	var speed = 'fast';

	// Smooth navigation between pages

	$('html, body').hide();

	$('html, body').fadeIn(speed, function() {
		$('a[href], button[href]').click(function(event) {
			var url = $(this).attr('href');
			if (url.indexOf('#') == 0 || url.indexOf('javascript:') == 0)
				return;
			event.preventDefault();
			$('html, body').fadeOut(speed, function() {
				window.location = url;
			});
		});
	});

});

function selectAllCards() {
	$(".cardBox").addClass('cardBoxChecked');
}

function unselectAllCards() {
	$(".cardBox").remove('cardBoxChecked');
}

function handleStyle(id) {
	if ($(document.getElementById(id)).is(':checked')) {
		$(document.getElementById(id)).parent().parent().addClass(
				'cardBoxChecked')
	} else {
		$(document.getElementById(id)).parent().parent().removeClass(
				'cardBoxChecked')
	}
}