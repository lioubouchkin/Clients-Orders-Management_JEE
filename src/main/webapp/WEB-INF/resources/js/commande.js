$(document).ready(function() {
	$("#clientsList").hide();
});

$("input[name=nouveauClient]:radio").click(function() { // attack a click event on all radio buttons with name 'radiogroup'
//	if ($(this).val() == 'yes') {
//		$("#clientsList").hide();
//		$("#clientDetails").show();
//	} else {
//		$("#clientsList").show();
//		$("#clientDetails").hide();
//	}
	$("div#clientDetails").hide();
	$("div#clientsList").hide();
    var divId = jQuery(this).val();
    $("div#"+divId).show();
});