'use strict'
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Bildirimler/{a}/{randomId}').onWrite((change, context) => {

	const a = context.params.a;
	const randomId = context.params.randomId;


	const mesajId = admin.database().ref(`/Bildirimler/${a}/${randomId}/mesaj_id`).once('value');

	return mesajId.then(result => {
		const mesaj_id = result.val();
		console.log('mesajın idsi : ',mesaj_id);

		const mesajIcerik = admin.database().ref(`/Mesajlar/${mesaj_id}`).once('value');
		return mesajIcerik.then(result1 => {
			const gonderenKisiId = result1.val().gonderenKisiId;
			const aliciKisiId = result1.val().aliciKisiId;
			const mesaj_icerik = result1.val().mesajIcerik;
			console.log('mesajın içeriği : ',mesaj_icerik);
			console.log('alici idsi : ',aliciKisiId);
			const gonderenAdi = admin.database().ref(`/Kullanici/${gonderenKisiId}`).once('value');
			return gonderenAdi.then(result2 => {
				const gonderenKisi_Ad = result2.val().isim;

				const aliciTokenId = admin.database().ref(`/Kullanici/${aliciKisiId}`).once('value');
				return aliciTokenId.then(result3 => {
					const alici_token_id = result3.val().Token;
					console.log('gönderenin Adı : ',gonderenKisi_Ad);
					console.log('Token id : ',alici_token_id);
					const payload = {
						notification: {
								title:"Yeni Mesaj ! ",
								body:`${gonderenKisi_Ad} : ${mesaj_icerik}`,
								icon:"default",
								click_action:"com.mustafa.satgitsin_TARGET_NOTIFICATION"
						},
						data : {
								kisiId : gonderenKisiId,
								mesaj : mesaj_icerik,
								gonderenAd : gonderenKisi_Ad
						}
					};
					return admin.messaging().sendToDevice(alici_token_id,payload).then(response =>{
						console.log('Oldu');
						return null;
					});
				});


			});

		});
	});


	//myref.on("value", function(snapshot) {
	 //snapshot.forEach(function(childSnapshot) {
	 // var childData = childSnapshot.val();
	 // var mesaj_id=childData.mesaj_id;
	 // console.log(mesaj_id);
	 //});
	//});
	//console.log('randomId : ', randomId);

});
