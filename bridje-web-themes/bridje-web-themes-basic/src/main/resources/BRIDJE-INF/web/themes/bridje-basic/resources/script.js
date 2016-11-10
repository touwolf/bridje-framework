(function($)
{
    $(function()
    {
        window.initBridjeView = function()
        {

        };

        window.initViewForm = function(el)
        {
            var postView = function(event)
            {
                event.preventDefault();

                $('input[name=__action]').val($(event.target).data('action'));

                var container = $('#bridje-view-container');
                $.ajax(
                {
                    type: 'POST',
                    url: window.location,
                    data: container.serialize(),
                    success: function(response)
                    {
                        container.html(response);
                        initViewForm(container);
                        initBridjeView();
                        $(window).trigger( "load" );
                    }
                });
            };
            el.find('a.ajax-action').click(postView);
            el.find('button.ajax-action').click(postView);
            el.find('select.ajax-action').change(postView);
        };

        initViewForm($('#bridje-view-container'));
    });
})(jQuery);