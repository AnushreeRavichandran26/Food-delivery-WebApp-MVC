// Checkout page functionality - payment method selection and validation
function selectPayment(method, element) {
    // Remove selected class from all options
    document.querySelectorAll('.payment-option').forEach(option => {
        option.classList.remove('selected');
    });

    // Add selected class to clicked option
    element.classList.add('selected');

    // Set the radio button value
    const radio = element.querySelector('input[type="radio"]');
    if (radio) {
        radio.checked = true;
    }

    // Hide all payment detail sections
    document.getElementById('cardDetails').classList.add('hidden');
    document.getElementById('upiDetails').classList.add('hidden');

    // Show relevant payment details
    if (method === 'card') {
        document.getElementById('cardDetails').classList.remove('hidden');
    } else if (method === 'upi') {
        document.getElementById('upiDetails').classList.remove('hidden');
    }
}

// Form validation
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('checkoutForm');

    if (form) {
        form.addEventListener('submit', function(e) {
            const selectedPayment = document.querySelector('input[name="paymentMethod"]:checked');

            if (!selectedPayment) {
                e.preventDefault();
                alert('Please select a payment method');
                return false;
            }

            // Validate card details if card payment is selected
            if (selectedPayment.value === 'card') {
                const cardNumber = document.getElementById('cardNumber').value;
                if (!cardNumber || cardNumber.trim().length < 16) {
                    e.preventDefault();
                    alert('Please enter a valid card number');
                    return false;
                }
            }

            // Validate UPI if UPI payment is selected
            if (selectedPayment.value === 'upi') {
                const upiId = document.getElementById('upiId').value;
                if (!upiId || !upiId.includes('@')) {
                    e.preventDefault();
                    alert('Please enter a valid UPI ID');
                    return false;
                }
            }
        });
    }
});