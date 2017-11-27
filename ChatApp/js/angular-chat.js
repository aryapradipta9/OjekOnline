function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i < ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') {
          c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
          return c.substring(name.length, c.length);
      }
  }
  return "";
}

$(document).ready(function(){
  var usrnm = getCookie("username");
  var usrnmdrv = getCookie("usrnmdrv");
  //console.log(usrnmdrv);
  loadMessages(usrnm, usrnmdrv);
});

firebase.messaging().onMessage(function(payload) {
  console.log("Message received. ", payload);
  if(payload.data.message == "chat-finished"){
    window.location.replace("index.html");
  }else{
    updateChat(payload.data.message, false);
  }
});


var chat = angular.module('chatApp', []);
//var cookie = angular.module('chatApp', ['ngCookies']);

chat.controller('chatCtrl', ['$scope', function($scope) {
    $scope.messages = [];
    $scope.username = getCookie("user");
    var usrnm = getCookie("username");
    var usrnmdrv = getCookie("usrnmdrv");
    $scope.send = function() {
        $scope.messages.push({id:$scope.messages.length, message:$scope.chatInput, type:'message-sent'});
        $.post('http://localhost:3000/chat/' + usrnm + '/' + usrnmdrv,{"id": $scope.messages.length, "message": $scope.chatInput, type:'message-sent'},
          function(data, status) {
            console.log('data: ' + data);
          });
        $scope.chatInput = '';
    };
    $scope.update = function(msg, me) {
    	var typ = '';
    	if (me) {
    		typ = 'message-sent';
    	} else {
    		typ = 'message-received';
    	}
    	$scope.messages.push({id:$scope.messages.length, message:msg, type:typ});
    }
}]);

function updateChat(msg, me) {
	var scope = angular.element($(".chat-display")).scope();
	scope.$apply(function() {
		scope.update(msg,me);
	})
}

// function getURL(){
//     var urlString = window.location.href;
//     console.log(urlString);
//     var url = new URL(urlString);
//     var usrnmdrv = url.searchParams.get("usrnmdrv");
//     console.log(usrnmdrv);
// }

function loadMessages(usrnm, usrnmdrv) {
    
	$.ajax({
      type:'GET',
      url:'http://localhost:3000/chat/' + usrnm + '/' + usrnmdrv,
      dataType: 'json',
      success: function(result){
          var data = result;
        console.log('data: ' + data);
    	}
   	})
   	.then(function(data) {
   		hist = JSON.stringify(data);
   		if (hist.length != 0) {
   			$.each(hist, function(index, value) {
   				updateChat(value.message, value.type);
   				console.log(value);
   			})
   		}
   	});
}

//cookie.controller('cookieCtrl', ['$scope', '$cookies', function($cookies) {
//}]);