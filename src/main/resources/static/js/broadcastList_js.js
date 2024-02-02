document.querySelectorAll('.view-details-button').forEach(function (button) {
    button.addEventListener('click', function () {
        const broadcastId = button.getAttribute('data-broadcast-id');
        window.location.href = '/broadcasts/' + broadcastId;
    });
});