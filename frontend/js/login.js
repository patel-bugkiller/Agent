// Utility function to show messages matching your existing style
function showMessage(text, isSuccess) {
    const messageBox = document.getElementById('messageBox');
    messageBox.textContent = text;
    messageBox.className = 'message-box ' + (isSuccess ? 'success' : 'error');
    messageBox.classList.remove('hidden');
}

document.getElementById('loginForm').addEventListener('submit', function (event) {
    // Prevent normal form submission/reload
    event.preventDefault();

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    if (!email || !password) {
        showMessage('Email and password are required.', false);
        return;
    }

    // Format data as URL-encoded to match our backend handler
    const formData = new URLSearchParams();
    formData.append('email', email);
    formData.append('password', password);

    // Send the data to the Java login backend
    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData.toString()
    })
        .then(response => {
            if (response.ok) {
                return response.text().then(text => ({ success: true, message: text }));
            } else {
                return response.text().then(text => ({ success: false, message: text }));
            }
        })
        .then(data => {
            showMessage(data.message, data.success);
            if (data.success) {
                // Optional: redirect to a dashboard if you want!
                document.getElementById('loginForm').reset();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showMessage('An error occurred while communicating with the server.', false);
        });
});
