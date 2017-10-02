  
$(function() {
	var slideqty = $('#featured .item').length;
	for (var i=0; i < slideqty; i++) {
		var insertText = '<li data-target="#featured" data-slide-to="' + i + '"></li>';
		$('#featured ol').append(insertText);
	}
	$('.carousel').carousel({
		interval: false
	});

})