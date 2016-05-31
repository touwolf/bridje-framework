
var init = function(el)
{
    el.find("button.action").click(function(event)
    {
        event.preventDefault();
        $('input[name=__action]').val($(event.target).data("action"));
        $.ajax({
            type : 'POST',
            url : window.location,
            data : $("form#view-form").serialize(),
            success : function(data, status, jqXHR)
            {
                $("form#view-form").html(data);
                init($("form#view-form"));
            }
        });
    });
};

$(document).ready(function()
{
    init($("form#view-form"));
});