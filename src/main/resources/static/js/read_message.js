
setInterval(readMessages, 1000);

function readMessages() {
    var token = $("meta[name='_csrf']").attr("content");
    let unreadMessages = [];
    let messages = document.querySelectorAll('.unread');
    messages.forEach(message => {
        if (!message.classList.contains("income")) {
            unreadMessages.push(message.parentElement.id);
        }
    });
    $.ajax({
        type: 'POST',
        url: '/readMessages',
        data: {messagesToRead : JSON.stringify(unreadMessages), _csrf : token}
    });
}


