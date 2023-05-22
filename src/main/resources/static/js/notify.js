notifySet();
getChatDetails();

let me;

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
            me = result.me;
        }
    });
}

function getNotifications() {
    $.ajax({
        type: 'GET',
        url: '/notification',
        contentType: 'application/json',
        success: function (result) {
            sendNotifyMessage(result);
        }
    })
}

function sendNotifyMessage(result) {
    if (result === "") return;
    console.log(result);
    console.log(me.id);
    if (result.personId !== me.id) return;
    var notification = new Notification("Новое сообщение", {
        tag: result.tag,
        body: result.message,
        url: "http://45.90.34.91/"
    })
}

function notifyMe() {
    setInterval(getNotifications, 2000);
}

function notifySet() {
    console.log("notifySet is on");
    console.log(Notification.permission);
    if (!"Notification" in window) {
        alert("Ваш браузер не поддерживает уведомления")
    } else if (Notification.permission === 'granted') {
        setTimeout(notifyMe, 2000);
    } else if (Notification.permission !== 'denied') {
        Notification.requestPermission(function (permission) {
            if (!('permission' in Notification)) {
                Notification.permission = permission;
            }
            if (permission === 'granted') {
                setTimeout(notifyMe, 2000);
            }
        });
    }

}