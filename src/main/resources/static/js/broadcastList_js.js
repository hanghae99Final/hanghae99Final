document.querySelectorAll('.view-details-button').forEach(function (button) {
    button.addEventListener('click', function () {
        const broadcastId = button.getAttribute('data-broadcast-id');
        window.location.href = '/broadcasts/' + broadcastId;
    });
});

document.querySelectorAll('.broadcast-start-button').forEach(function (button) {
    const isAuthenticated = checkCookieExistence('Authorization');

    button.addEventListener('click', function () {
        if (!isAuthenticated) {
            alert('로그인 후에 방송할 수 있습니다.');
            window.location.href = '/api/user/login-page';
        }

        window.location.href = '/broadcasts/start';
    });
});

function checkCookieExistence(cookieName) {
    return document.cookie.split(';').some((item) => item.trim().startsWith(cookieName + '='));
}
