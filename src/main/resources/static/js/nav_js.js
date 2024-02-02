function checkCookie(name) {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
        const [cookieName, cookieValue] = cookie.trim().split('=');
        if (cookieName === name) {
            return true;
        }
    }
    return false;
}

window.addEventListener("DOMContentLoaded", () => {
    const isAuthenticated = checkCookie('Authorization');

    const navbar = document.querySelector('.navbar');
    navbar.innerHTML = '';

    const homeLink = document.createElement('a');
    homeLink.href = '/';
    homeLink.textContent = 'Home';
    navbar.appendChild(homeLink);

    if (isAuthenticated) {
        const myPageLink = document.createElement('a');
        myPageLink.href = '/my-page';
        myPageLink.textContent = 'MyPage';
        navbar.appendChild(myPageLink);

        const logoutLink = document.createElement('a');
        logoutLink.textContent = 'Logout';
        logoutLink.addEventListener('click', function () {
            deleteCookie('Authorization');
        });
        logoutLink.href = '/';
        navbar.appendChild(logoutLink);
    } else {
        const loginLink = document.createElement('a');
        loginLink.href = '/api/user/login-page';
        loginLink.textContent = 'Login';
        navbar.appendChild(loginLink);
    }
});

function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}