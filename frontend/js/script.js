document.getElementById('registerForm').addEventListener('submit', function (event) {
    // Prevent normal form submission/reload
    event.preventDefault();

    const phoneNumber = document.getElementById('phoneNumber').value.trim();
    const email = document.getElementById('email').value.trim();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const messageBox = document.getElementById('messageBox');

    // Basic client-side validation
    if (!phoneNumber || !email || !username || !password) {
        showMessage('All fields are required.', false);
        return;
    }

    if (password.length < 6) {
        showMessage('Password must be at least 6 characters.', false);
        return;
    }

    // Format data as URL-encoded to match our backend handler
    const formData = new URLSearchParams();
    formData.append('phone_number', phoneNumber);
    formData.append('email', email);
    formData.append('username', username);
    formData.append('password', password);

    // Send the data to the Java backend
    fetch('http://localhost:8080/api/register', {
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
        .then(result => {
            if (result.success) {
                showMessage(result.message, true);
                document.getElementById('registerForm').reset(); // Clear the form on success
            } else {
                showMessage(result.message, false);
            }
        })
        .catch(error => {
            showMessage('Failed to connect to the server. Is the Java backend running?', false);
        });
});

function showMessage(message, isSuccess) {
    const messageBox = document.getElementById('messageBox');
    messageBox.textContent = message;
    messageBox.className = 'message-box ' + (isSuccess ? 'success' : 'error');
}
