
$(document).ready(function()
{
    var initForm = function(el)
    {
        el.find('button.action').click(function(event)
        {
            event.preventDefault();

            $('input[name=__action]').val($(event.target).data('action'));

            $.ajax({
                type: 'POST',
                url: window.location,
                data: $('form#view-form').serialize(),
                success: function(response)
                {
                    $('form#view-form').html(response);
                    initForm($('form#view-form'));
                }
            });
        });
    };

    initForm($('form#view-form'));
    window.__initForm = initForm;
});
