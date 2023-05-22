let messagesList = $('.message-holder');
const mediaQuery = window.matchMedia('(max-width: 768px)');


setupBurgerButton();
becomeMessages();
setMessageBlockHeight();
getChatDetails();
setSendButton();
setTimeout(setScroll, 100);

function setScroll() {
    let block = document.querySelector('#messages_block');
    if (block === null) return;
    block.scroll(0, Number.MAX_SAFE_INTEGER);
}

function setSendButton() {
    let sendButton = $('#send_button');
    sendButton.click('click', function () {
        let messageField = document.querySelector('#new_message');
        var token = $("meta[name='_csrf']").attr("content");
        $.ajax({
            type: 'POST',
            url: '/messages',
            data: {textMessage: messageField.value, _csrf : token}
        })
        setTimeout(() => getMessages(), 100);
        setTimeout(setScroll, 300);
        messageField.value = "";
    })
}

function setControls() {
    $(document).off("click", ".message-controls");
    $(document).on("click", ".message-controls", function () {

        let messageId = this.parentNode.parentNode.id;
        if (confirm("Действительно хотите удалит сообщение?")) {
            deleteMessage(messageId);
            setTimeout(() => getMessages(), 100);
        }
    })
}

function deleteMessage(messageId) {
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: 'DELETE',
        url: "/messages/" + messageId,
        data: {_csrf : token}
    })
}

window.addEventListener('resize', function(event){
    calcMessageBoxSize();
})

function setupControls() {
    let messageControls = messagesList.filter(".message-controls");
    messageControls.each(function () {
        console.log($(this).parent());
    })
    if(messageControls == null) return;
    messageControls.each(function (){
        console.log($(this));
        $(document).on("click", ".message-controls", function () {
            let parent = $(this).parentNode;
            console.log(parent);
            let display = parent.querySelector(".message-controls-display");
            switchDisplay(display);
        });
    });
}

function switchDisplay(el) {
    let style = window.getComputedStyle(el);
    let display = style.getPropertyValue('display');
    console.log("switch");
    if (display === 'none') {
        el.style.display = 'block';
    } else {
        el.style.display = 'none';
    }
}

var tx = document.getElementsByTagName('textarea');

for (var i = 0; i < tx.length; i++) {

tx[i].setAttribute('style', 'height:' + (tx[i].scrollHeight) + 'px;overflow-y:hidden;');

tx[i].addEventListener("input", OnInput, false);

}

function OnInput() {

this.style.height = 'auto';

this.style.height = (this.scrollHeight) + 'px';

}

function setupBurgerButton() {
    const burgerButton = document.querySelector('.chatbox__header-burger-button');
    const contactsBlock = document.querySelector('.chatbox__contacts-block');
    if (burgerButton == null) return;
    burgerButton.addEventListener('click', function(event) {
        switchDisplay(contactsBlock);
    })
}

if(mediaQuery.matches) {
    calcMessageBoxSize();
}

function calcMessageBoxSize() {
    const messageBox = document.querySelector('.chatbox__messages-block');
    if (messageBox == null) return;
    const chatboxHeader = document.querySelector('.chatbox__header');
    const chatboxInput = document.querySelector('.chatbox__input-space');
    let screenHeight = window.innerHeight;
    let messageBoxSize = screenHeight - chatboxInput.scrollHeight - chatboxHeader.scrollHeight + 3;
    messageBox.style.height = messageBoxSize + 'px';
}

function getChatDetails() {
    const dstPersonName = document.querySelector('.dst_person');
    if (dstPersonName == null) return;
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: 'GET',
        url: '/chatDetails',
        data: {_csrf : token},
        success: function (result) {
            $('.dst_person').html(result.dstPerson.name + " " + result.dstPerson.firstName);
        }
    });
}

function getMessages() {
    const messagesBlock = document.querySelector('#messages_block');
    if (messagesBlock == null) return;
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: 'GET',
        url: '/messages',
        data: {_csrf : token},
        success: function (result) {
            $('#messages_block').html(getMessagesHtml(result));
        }
    });
    setControls();
    // setTimeout(() => setScroll(), 100);
}

function getMessagesHtml(result) {

    let messagesHtml = "";

    for (let messageRsItem of result) {
        let income = "";
        if (messageRsItem.income) {
            income = " income"
        }
        messagesHtml += "    <div id=\"" + `${messageRsItem.id}` + "\" class=\"message-holder\">\n" +
            "        <div class=\"message-body" + `${income}` + "\">\n" +
            "            <div class=\"message-controls\">&rsaquo;</div>\n" +
            "            <div class=\"message-controls-display\">\n" +
            "                <a href=\"#\">\n" +
            "                    <p class=\"message-control-item\">Удалить</p>\n" +
            "                </a>\n" +
            "            </div>\n" +
            "            <div class=\"message-text\">" + `${messageRsItem.messageText}` + "</div>\n" +
            "            <div class=\"message-date-time\">" + `${messageRsItem.time}` + "</div>\n" +
            "        </div>\n" +
            "    </div>"
    }

    return messagesHtml;
}

function becomeMessages() {
    getMessages();
    setInterval(() => getMessages(), 1500);
}

function setMessageBlockHeight() {
    if (mediaQuery.matches) return;
    const messagesBlock = document.querySelector('#messages_block');
    if(messagesBlock === null) return;
    let height = window.screen.height * 0.7;
    messagesBlock.style.height = height.toString() + "px";
}

