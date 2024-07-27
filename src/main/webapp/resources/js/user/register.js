	
 document.addEventListener("DOMContentLoaded", function(){
	console.log("────────DOMContentLoaded────────");
	
	const userIdInput = document.querySelector("#userId");
	const nameInput = document.querySelector("#name");
	const passwordInput = document.querySelector("#password");
	const passwordCheckInput = document.querySelector("#passwordCheck");
	const emailInput = document.querySelector("#email");
	const birthInput = document.querySelector("#birth");
	const addrInput = document.querySelector("#sample4_roadAddress");
	const detailAddrInput = document.querySelector("#sample4_detailAddress");
	
	//비밀번호 확인 버튼
	let idCheckCount = 0;
	let passwordCheckCount = 0;
	
	//변수
	const doSaveBtn = document.querySelector("#doSave");
	console.log("doSaveBtn", doSaveBtn);
	
	const idCheckBtn =  document.querySelector("#idDuplicateCheck");
	console.log("idCheckBtn", idCheckBtn);
	
	const passwordCheckBtn =  document.querySelector("#passwordDuplicateCheck");
	console.log("passwordCheckBtn", passwordCheckBtn);
	
	//이벤트 리스너
	doSaveBtn.addEventListener("click",function(event){
		console.log("doSaveBtn click");
		
		doSave();
	});
	
	idCheckBtn.addEventListener("click",function(event){
		console.log("idCheckBtn click");
		
		idCheck();
	});
	
	passwordCheckBtn.addEventListener("click",function(event){
		console.log("passwordCheckBtn click");
		
		passwordCheck();
	});
	  
	function doSave() {
        console.log("doSave()");
        
		if(isEmpty(userIdInput.value) == true){
			alert("사용하실 아이디를 입력하세요.");
			userIdInput.focus();
			return;
		}
		if(isEmpty(nameInput.value) == true){
			alert("사용하실 닉네임을 입력하세요.");
			nameInput.focus();
			return;
		}
		if(isEmpty(passwordInput.value) == true){
			alert("비밀번호를 입력하세요.");
			passwordInput.focus();
			return;
		}
		if(isEmpty(passwordCheckInput.value) == true){
			alert("비밀번호를 확인하세요");
			passwordCheckInput.focus();
			return;
		}
		if(isEmpty(emailInput.value) == true){
			alert("이메일을 입력하세요.");
			emailInput.focus();
			return;
		}
		if(isEmpty(birthInput.value) == true){
			alert("생년월일을 입력하세요.");
			birthInput.focus();
			return;
		}
		if(isEmpty(addrInput.value) == true){
			alert("주소를 입력하세요.");
			addrInput.focus();
			return;
		}
		if(isEmpty(detailAddrInput.value) == true){
			alert("상세주소를 입력하세요.");
			detailAddrInput.focus();
			return;
		}
		
		if(idCheckCount != 1){
			alert("아이디 중복을 확인하세요.");
			return;
		}
		
		if(passwordCheckCount != 1){
			alert("비밀번호가 일치하지 않습니다.");
			passwordCheckBtn.focus();
			return;
		}
		
		
		let type="POST";
        let url ="/doma/user/doSave.do";
        let async = "true";
        let dataType = "html";

        if(confirm("등록 하시겠습니까?") === false)return;

        let params = {
            "userId" : userIdInput.value,
            "name" : nameInput.value,
            "password" : passwordInput.value,
            "email" : emailInput.value,
            "birth" : birthInput.value,
            "address" : addressInput.value,
            "address_2" : detailAddressInput.value
        }

        PClass.pAjax(url,params,dataType,type,async,function(data){
            if(data){
                try{
                    //문자열 Json Object로 변환
                    const message = JSON.parse(data);
                    if(isEmpty(message) === false && 1== message.messageId){
                    	console.log(message.messageContent);
                    	console.log(message.messageId);
                        alert(message.messageContents);
                        //commumity_page 이동하는 구문
                    }else{
                        alert(message.messageContents);
                    }
                }catch(e){
                    console.error("data가 null 혹은, undefined 입니다");
                    alert("data가 null 혹은, undefined 입니다.");
                }
            }else{
                console.log("else");
                alert("data가 null 혹은, undefined 입니다.");
            }
        });
	}
	
	function idCheck() {
		console.log("idCheck()");
		
		let type="GET";
        let url ="/doma/user/idCheck.do";
        let async = "true";
        let dataType = "html";

        if(confirm("아이디를 사용하시겠습니까?") === false)return;

        let params = {
            "userId" : userId
        }
		
		console.log("userId : " + userId);
		 
        PClass.pAjax(url,params,dataType,type,async,function(data){
            if(data){
                try{
                    //문자열 Json Object로 변환
                    const message = JSON.parse(data);
                    if(isEmpty(message) === false && 1== message.messageId){
                    	console.log(message.messageContents);
                    	console.log(message.messageId);
                        alert(message.messageContents);
                        idCheckCount= 1;
                    }else{
                        alert(message.messageContents);
                    }
                }catch(e){
                    console.error("JSON 파싱 오류:", e);
                }
            }else{
                console.log("else");
                alert("data가 null 혹은, undefined 입니다.");
            }
        });
	}
	
	function passwordCheck() {
		console.log("passwordCheck()");
	}
	  
});
        