let messages = document.querySelectorAll('.message-body');
let messageBlock = document.querySelector('#messages_block');

// readMessages();

var Visible = function (message) {
    var targetPosition = {
            top: messageBlock.scrollTop + message.getBoundingClientRect().top
        },
        blockPosition = {
            top: messageBlock.scrollTop
        };
    if (targetPosition.bottom > blockPosition.top) { // Если позиция левой стороны элемента меньше позиции правой чайти окна, то элемент виден справа
        console.clear();
        console.log(message.id);
    } else {
        console.clear();
    };
};

function isElementInViewport (el) {

    let rect = el.getBoundingClientRect();

    return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (messageBlock.scrollTop)
    );
}

messageBlock.addEventListener('scroll', function() {
    readMessages();
})


function readMessages() {

    messages.forEach(function (message) {
        console.log(isElementInViewport (message));
    })
}


