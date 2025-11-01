// Menu page functionality - quantity controls and add to cart
let quantities = {};

function increaseQuantity(button) {
    const itemId = button.dataset.itemId;
    const itemName = button.dataset.itemName;
    const itemPrice = parseFloat(button.dataset.itemPrice);
    const restaurantId = button.dataset.restaurantId;
    const restaurantName = button.dataset.restaurantName;

    // Update display
    const qtyDisplay = button.parentElement.querySelector('.qty-display');
    const currentQty = parseInt(qtyDisplay.textContent);
    qtyDisplay.textContent = currentQty + 1;

    // Add to cart via AJAX
    fetch('/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `itemId=${itemId}&itemName=${encodeURIComponent(itemName)}&price=${itemPrice}&restaurantId=${restaurantId}&restaurantName=${encodeURIComponent(restaurantName)}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                updateCartCount(data.cartCount);
            }
        })
        .catch(error => console.error('Error adding to cart:', error));
}

function decreaseQuantity(button) {
    const qtyDisplay = button.parentElement.querySelector('.qty-display');
    const currentQty = parseInt(qtyDisplay.textContent);

    if (currentQty > 0) {
        qtyDisplay.textContent = currentQty - 1;
        // Note: You'd need to add logic to remove from cart
        // For now, just update display
        loadCart();
    }
}

function toggleCart() {
    document.getElementById('cartSidebar').classList.toggle('active');
    document.getElementById('overlay').classList.toggle('hidden');
}

function loadCart() {
    fetch('/cart/get')
        .then(response => response.json())
        .then(data => {
            updateCartDisplay(data.cart, data.total);
            updateCartCount(data.cartCount);
        })
        .catch(error => console.error('Error loading cart:', error));
}

function updateCartDisplay(cart, total) {
    const cartItems = document.getElementById('cartItems');

    if (!cart || cart.length === 0) {
        cartItems.innerHTML = '<div class="empty-cart">Your cart is empty</div>';
    } else {
        cartItems.innerHTML = cart.map((item, index) => `
            <div class="cart-item">
                <div>
                    <div class="cart-item-name">${item.name}</div>
                </div>
                <div>
                    <span class="cart-item-price">₹${item.price}</span>
                    <button class="cart-remove-btn" onclick="removeFromCart(${index})">Remove</button>
                </div>
            </div>
        `).join('');
    }

    document.getElementById('cartTotal').textContent = '₹' + (total || 0);
}

function updateCartCount(count) {
    const cartCounts = document.querySelectorAll('.cart-count');
    cartCounts.forEach(el => {
        el.textContent = count || 0;
    });
}

function removeFromCart(index) {
    fetch('/cart/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `index=${index}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadCart();
            }
        })
        .catch(error => console.error('Error removing item:', error));
}

// Load cart on page load
document.addEventListener('DOMContentLoaded', loadCart);