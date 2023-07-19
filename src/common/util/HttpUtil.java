package common.util;

public class HttpUtil {
	
	public static String getHttpStatus(int statusCode) {
		switch(statusCode) {
			case 0 : 
				return "Unable to Process";
		
			case 100 :
				// 진행 중임을 의미하는 응답코드입니다. 현재까지의 진행상태에 문제가 없으며, 클라이언트가 계속해서 요청을 하거나 이미 요청을 완료한 경우에는 무시해도 되는 것을 알려줍니다.
				return "Continue";
				
			case 101 :
				// 101은 클라이언트에 의해 보낸 업그레이드 요청 헤더에 대한 응답으로 보내집니다.
				// 이 응답 코드는 클라이언트가 보낸 Upgrade 요청 헤더에 대한 응답에 들어가며, 서버에서 프로토콜을 변경할 것임을 알려줍니다. 해당 코드는 Websocket 프로토콜 전환 시에 사용됩니다.
				return "Switching Protocol";
				
			case 102 :
				// 이 응답 코드는 서버가 요청을 수신하였으며 이를 처리하고 있지만, 아직 제대로 된 응답을 알려줄 수 없음을 알려줍니다.
				return "Processing(WebDAV)";
				
			case 200 : 
				// 요청이 성공적으로 되었습니다. 정보는 요청에 따른 응답으로 반환됩니다.
				return "OK";
				
			case 201 : 
				// 요청이 성공적이었으며 그 결과로 새로운 리소스가 생성되었습니다. 이 응답은 일반적으로 POST 요청 또는 일부 PUT 요청 이후에 따라옵니다.
				return "Created";
				
			case 202 :
				// 요청을 수신하였지만, 그에 응하여 행동할 수 없습니다. 이 응답은 요청 처리에 대한 결과를 이후에 HTTP로 비동기 응답을 보내는 것에 대해서 명확하게 명시하지 않습니다.
				// 이것은 다른 프로세스에서 처리 또는 서버가 요청을 다루고 있거나 배치 프로세스를 하고 있는 경우를 위해 만들어졌습니다.
				return "Accepted";
				
			case 203 : 
				// 이 응답 코드는 돌려받은 메타 정보 세트가 오리진 서버의 것과 일치하지 않지만 로컬이나 서드 파티 복사본에서 모아졌음을 의미합니다. 
				// 이러한 조건에서는 이 응답이 아니라 200 OK 응답을 반드시 우선됩니다.
				return "Non-Authoritative Information";
				
			case 204 : 
				// 요청에 대해서 보내줄 수 있는 콘텐츠가 없지만, 헤더는 의미있을 수 있습니다. 
				// 사용자-에이전트는 리소스가 캐시된 헤더를 새로운 것으로 업데이트 할 수 있습니다.
				return "No Content";
				
			case 205 :
				// 이 응답 코드는 요청을 완수한 이후에 사용자 에이전트에게 이 요청을 보낸 문서 뷰를 리셋하라고 알려줍니다.
				return "Reset Content";

			case 206 :
				// 이 응답 코드는 클라이언트에서 복수의 스트림을 분할 다운로드를 하고자 범위 헤더를 전송했기 때문에 사용됩니다.
				// 클라이언트가 이어받기를 시도하면 웹서버가 이에 대한 응답코드로 '206 Partial Content'와 함께 Range 헤더에 명시된 데이터의 부분(byte)부터 전송을 시작합니다.
				return "Partial Content";

			case 207 :
				// 멀티-상태 응답은 여러 리소스가 여러 상태 코드인 상황이 적절한 경우에 해당되는 정보를 전달합니다. 
				// 해당 코드는 WebDAV(Web Distributed Authoring and Vesioning)에 사용됩니다.
				return "Multi-Status";

			case 208 :
				// Prostat(property와 status의 합성어) 응답 속성으로 동일 컬렉션으로 바인드된 복수의 내부 멤버를 반복적으로 열거하는 것을 피하기 위해 사용됩니다. 
				// 해당 코드는 WebDAV(Web Distributed Authoring and Vesioning)에 사용됩니다.
				return "Already Reported";

			case 226 :
				// 서버가 GET 요청에 대한 리소스의 의무를 다 했고, 그리고 응답이 하나 또는 그 이상의 인스턴스 조작이 현재 인스턴스에 적용이 되었음을 알려줍니다.
				return "IM Used ( HTTP Delta encoding )";

			case 300 :
				// 요청에 대해서 하나 이상의 응답이 가능합니다. 
				// 사용자 에언트 또는 사용자는 그중에 하나를 반드시 선택해야 합니다. 
				// 응답 중 하나를 선택하는 방법에 대한 표준화 된 방법은 존재하지 않습니다.
				return "Multiple Choice";

			case 301 :
				// 이 응답 코드는 요청한 리소스의 URI가 변경되었음을 의미합니다.
				// 새로운 URI가 응답에서 아마도 주어질 수 있습니다.
				return "Moved Permanently";

			case 302 :
				// 이 응답 코드는 요청한 리소스의 URI가 일시적으로 변경되었음을 의미합니다.
				// 새롭게 변경된 URI는 나중에 만들어질 수 있습니다. 
				// 그러므로, 클라이언트는 향후의 요청도 반드시 동일한 URI로 해야합니다.
				return "Found";

			case 303 :
				// 클라이언트가 요청한 리소스를 다른 URI에서 GET 요청을 통해 얻어야 할 때, 서버가 클라이언트로 직접 보내는 응답입니다.
				return "See Other";

			case 304 :
				// 이것은 캐시를 목적으로 사용됩니다. 
				// 이것은 클라이언트에게 응답이 수정되지 않았음을 알려주며, 
				// 그러므로 클라이언트는 계속해서 응답의 캐시된 버전을 사용할 수 있습니다.
				return "Not Modified";

			case 305 :
				// 이전 버전의 HTTP 기술 사양에서 정의되었으며, 요청한 응답은 반드시 프록시를 통해서 접속해야 하는 것을 알려줍니다. 이것은 프록시의 in-band설정에 대한 보안상의 걱정으로 인하여 사라져가고 있습니다.
				return "Use Proxy";

			case 306 :
				// 이 응답 코드는 더이상 사용되지 않으며, 현재는 추후 사용을 위해 예약되어 있습니다. 이것은 HTTP 1.1 기술사양 이전 버전에서 사용되었습니다.
				return "Unused";

			case 307 :
				// 클라이언트가 요청한 리소스가 다른 URI에 있으며, 이전 요청과 동일한 메소드를 사용하여 요청해야 할 때, 서버가 클라이언트에 이 응답을 직접 보냅니다. 
				// 이것은 302 Found HTTP 응답 코드와 동일한 의미를 가지고 있으며, 사용자 에이전트가 반드시 사용된 HTTP 메소드를 변경하지 말아야 하는 점만 다릅니다. 
				// 만약 첫 요청에 POST가 사용되었다면, 두번째 요청도 반드시 POST를 사용해야 합니다.
				return "Temporary Redirect";

			case 308 :
				// 이것은 리소스가 이제 HTTP 응답 헤더의 Location:에 명시된 영구히 다른 URI에 위치하고 있음을 의미합니다. 이것은 301 Moved Permanently HTTP 응답 코드와 동일한 의미를 가지고 있으며, 
				// 사용자 에이전트가 반드시 HTTP 메소드를 변경하지 말아야 하는 점만 다릅니다. 
				// 만약 첫 요청에 POST가 사용되었다면, 두번째 요청도 반드시 POST를 사용해야 합니다.
				return "Permanent Redirect";
				
			case 400 :
				// 이 응답은 잘못된 문법으로 인하여 서버가 요청하여 이해할 수 없음을 의미합니다.
				return "Bad Request";				

			case 401 :
				// 비록 HTTP 표준에서는 '미승인(unauthorized)'를 명확히 하고 있지만, 
				// 의미상 이 응답은 '비인증(unauthenticated)'를 의미합니다. 
				// 클라이언트는 요청한 응답을 받기 위해서는 반드시 스스로를 인증해야 합니다.
				return "Unauthorized";

			case 402 :
				// 이 응답 코드는 나중에 사용될 것을 대비해 예약되었습니다. 
				// 첫 목표로는 디지털 결제 시스템에 사용하기 위하여 만들어졌지만 지금 사용되고 있지는 않습니다.
				return "Payment Required";				

			case 403 :
				// 클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않습니다. 
				// 예를 들어, 그들은 미승인이어서 서버는 거절을 위한 적절한 응답을 보냅니다. 
				// 401과 다른 점은 서버가 클라이언트가 누구인지 알고 있습니다.
				return "Forbidden";

			case 404 :
				// 서버는 요청받은 리소스를 찾을 수 없습니다. 브라우저에서는 알려지지 않은 URL을 의미합니다. 이것은 API에서 종점은 적절하지만 리소스 자체는 존재하지 않음을 의미할 수 있습니다. 
				// 서버들은 인증받지 않은 클라이언트로부터 리소스를 숨기기 위하여 이 응답을 403 대신에 전송할 수도 있습니다. 
				// 이 응답 코드는 웹에서 반복적으로 발생하기 때문에 가장 유명할지도 모릅니다.
				return "Not Found";

			case 405 :
				// 요청한 메소드는 서버에서 알고 있지만, 제거되었고 사용할 수 없습니다. 
				// 예를 들어, 어떤 API에서 리소스를 삭제하는 것을 금지할 수 있습니다. 
				// 필수적인 메소드인 GET과 HEAD는 제거될 수 없으며, 이 에러 코드를 리턴할 수 없습니다.
				return "Method Not Allowed";				

			case 406 :
				// 이 응답은 서버가 서버 주도 콘텐츠 협상을 수행한 후, 
				// 사용자 에이전트에서 정해준 규격에 따른 어떠한 콘텐츠도 찾지 않았을 때, 웹서버가 보냅니다.
				return "Not Acceptable";

			case 407 :
				// 이것은 401과 비슷하지만 프록시에 의해 완료된 인증이 필요합니다.
				return "Proxy Authentication Required";
				
			case 408 :
				// 이 응답은 요청을 한 지 시간이 오래된 연결에 일부 서버가 전송하며, 어떤 때에는 이전에 클라이언트로부터 어떠한 요청이 없었다고 하더라도 보내지기도 합니다. 
				// 이것은 서버가 사용되지 않는 연결을 끊고 싶어하는 것을 의미합니다. 
				// 이 응답은 특정 몇몇 브라우저에서 빈번하게 보이는데 Chrome, Firefox 27+, 또는 IE 9와 같은 웹서핑 속도를 올리기 위해 HTTP 사전 연결 메카니즘을 사용하는 브라우저들이 해당됩니다. 
				// 또한 일부 서버는 이 메시지를 보내지 않고 연결을 끊어버리기도 합니다.
				return "Request Timeout";

			case 409 :
				// 이 응답은 요청이 현재 서버의 상태와 충돌될 때 보냅니다.
				return "Conflict";

			case 410 :
				// 이 응답은 요청한 콘텐츠가 서버에서 영구적으로 삭제되었으며, 전달해 줄 수 있는 주소 역시 존재하지 않을 때 보냅니다. 
				// 클라이언트가 그들의 캐시와 리소스에 대한 링크를 지우기를 기대합니다. 
				// HTTP 기술 사양은 이 상태 코드가 '일시적인, 홍보용 서비스'에 사용되기를 기대합니다. 
				// API는 알려진 리소스가 이 상태 코드와 함께 삭제되었다고 강요해서는 안된다.
				return "Gone";

			case 411 :
				// 서버에서 필요로 하는 Content-Length 헤더 필드가 정의되지 않은 요청이 들어왔기 때문에 서버가 요청을 거절합니다.
				return "Length Required";

			case 412 :
				// 클라이언트의 헤더에 있는 전제조건은 서버의 전제조건에 적절하지 않습니다.
				return "Precondition Failed";

			case 413 :
				// 요청 엔티티는 서버에서 정의한 한계보다 큽니다. 
				// 서버는 연결을 끊거나 혹은 Retry-After 헤더 필드로 돌려보낼 것이다.
				return "Payload Too Large";

			case 414 :
				// 클라이언트가 요청한 URI는 서버에서 처리하지 않기로 한 길이보다 깁니다.
				return "URI Too Long";

			case 415 :
				// 요청한 미디어 포맷은 서버에서 지원하지 않습니다. 서버는 해당 요청을 거절할 것입니다.
				return "Unsupported Media Type";

			case 416 :
				// Range 헤더 필드에 요청한 지정 범위를 만족시킬 수 없습니다. 범위가 타겟 URI 데이터의 크기를 벗어났을 가능성이 있습니다.
				return "Requested Range Not Satisfiable";

			case 417 :
				// 이 응답 코드는 Expect 요청 헤더 필드로 요청한 예상이 서버에서는 적당하지 않음을 알려줍니다.
				return "Expectation Failed";

			case 418 :
				// 서버가 찻주전자이기 때문에 커피 내리기를 거절했다는 것을 의미합니다. 
				// 이 오류는 1998년 만우절 농담이었던 하이퍼 텍스트 커피 포트 제어 규약(Hyper Text Coffee Pot Control Protocol)의 레퍼런스입니다.
				return "I'm a teapot";

			case 421 :
				// 서버로 유도된 요청은 응답을 생성할 수 없습니다. 
				// 이것은 서버에서 요청 URI와 연결된 스킴과 권한을 구성하여 응답을 생성할 수 없을 때 보내집니다.
				return "Misdirected Request";

			case 422 :
				// 요청은 잘 만들어졌지만, 문법 오류로 인하여 따를 수 없습니다.
				return "Unprocessable Entity (WebDAV)";

			case 423 :
				// 리소스는 접근하는 것이 잠겨있습니다.
				return "Locked (WebDAV)";
				
			case 424 :
				// 이전 요청이 실패하였기 때문에 지금의 요청도 실패하였습니다.
				return "Failed Dependency (WebDAV)";

			case 426 :
				// 서버는 지금의 프로토콜을 사용하여 요청을 처리하는 것을 거절하였지만, 
				// 클라이언트가 다른 프로토콜로 업그레이드를 하면 처리를 할지도 모릅니다. 
				// 서버는 Upgrade 헤더와 필요로 하는 프로토콜을 알려주기 위해 426 응답에 보냅니다.
				return "Upgrade Required";
				

			case 428 :
				// 오리진 서버는 요청이 조건적이어야 합니다. 
				// 클라이언트가 리소스를 GET해서, 수정하고, 그리고 PUT으로 서버에 돌려놓는 동안 
				// 서드파티가 서버의 상태를 수정하여 발생하는 충돌인 '업데이트 상실'을 예방하기 위한 목적입니다.
				return "Precondition Required";
			case 429 :
				// 사용자가 지정된 시간에 너무 많은 요청을 보냈습니다("rate limiting").
				return "Too Many Requests";

			case 431 :
				// 요청한 헤더 필드가 너무 크기 때문에 서버는 요청을 처리하지 않을 것입니다. 
				// 요청은 크기를 줄인 다음에 다시 전송해야 합니다.
				return "Request Header Fields Too Large";

			case 451 :
				// 사용자가 요청한 것은 정부에 의해 검열된 웹페이지와 같은 불법적인 리소스입니다.
				return "Unavailable For Legal Reasons";

				
			case 500 :
				// 웹 사이트 서버에 문제가 있음을 의미하지만 서버는 정확한 문제에 대해 더 구체적으로 설명할 수 없습니다.
				return "Internal Server Error";

			case 501 :
				// 서버가 요청을 이행하는 데 필요한 기능을 지원하지 않음을 나타냅니다.
				return "Not Implemented";
				
			case 502 :
				// 서버가 게이트웨이로부터 잘못된 응답을 수신했음을 의미합니다. 인터넷상의 서버가 다른 서버로부터 유효하지 않은 응답을 받은 경우 발생합니다.
				return "Bad Gateway";

			case 503 :
				// 서버가 요청을 처리할 준비가 되지 않았습니다. 
				// 일반적인 원인은 유지보수를 위해 작동이 중단되거나 과부하가 걸린 서버입니다. 
				// 이 응답과 함께 문제를 설명하는 사용자 친화적인 페이지가 전송되어야 한다는 점에 유의하십시오.
				// 이 응답은 임시 조건에 사용되어야 하며, Retry-After: HTTP 헤더는 가능하면 서비스를 복구하기 전 예상 시간을 포함해야 합니다.
				// 웹마스터는 또한 이러한 일시적인 조건 응답을 캐시하지 않아야 하므로 이 응답과 함께 전송되는 캐싱 관련 헤더에 대해서도 주의해야 합니다.
				return "Service Unavailable";

			case 504 :
				// 웹페이지를 로드하거나 브라우저에서 다른 요청을 채우려는 동안 한 서버가 액세스하고 있는 다른 서버에서 적시에 응답을 받지 못했음을 의미합니다. 
				// 이 오류 응답은 서버가 게이트웨이 역할을 하고 있으며 적시에 응답을 받을 수 없을 경우 주어집니다. 
				// 이 오류는 대게 인터넷상의 서버 간의 네트워크 오류이거나 실제 서버의 문제입니다.
				// 컴퓨터, 장치 또는 인터넷 연결에 문제가 아닐 수 있습니다.
				return "Gateway Timeout";

			case 505 :
				// 서버에서 지원되지 않는 HTTP 버전을 클라이언트가 요청하였습니다. 
				// 대부분의 웹 브라우저는 웹 서버가 1.x 버전의 HTTP 프로토콜을 지원한다고 가정합니다. 
				// 실제로 1.0 이하의 매우 오래된 버전은 요즘 거의 사용되지 않습니다. 
				// 특히 최신 버전의 프로토콜보다 보안 및 성능이 좋지 않기 때문입니다. 
				// 따라서 웹 브라우저에서 이 오류가 표시되는 경우 웹 서버 소프트웨어에서 지원하는 HTTP 버전을 확인해 보아야 합니다.
				return "HTTP Version Not Supported";

			case 506 :
				// 서버에 내부 구성 오류가 있는 경우 발생합니다. 
				// 요청을 위한 투명한 콘텐츠 협상이 순환 참조로 이어집니다.
				return "Variant Also Negotiates";

			case 507 :
				// 선택한 가변 리소스는 투명한 서버에 내부 구성 요류가 있는 경우 발생합니다. 
				// 콘텐츠 협상에 참여하도록 구성되므로 협상 과정에서 적절한 끝점이 아닙니다.
				return "Insufficient Storage";

			case 508 :
				// 서버가 요청을 처리하는 동안 무한 루프를 감지한 경우 발생합니다.
				return "Loop Detected (WebDAV)";

			case 510 :
				// 서버가 요청을 이행하려면 요청에 대한 추가 확장이 필요합니다.
				return "Not Extended";

			case 511 :
				// 511 상태 코드는 클라이언트가 네트워크 액세스를 얻기 위해 인증할 필요가 있음을 나타냅니다.
				return "Network Authentication Required";
				
			default :
				return "Unknown";
		}
	}
	
}
