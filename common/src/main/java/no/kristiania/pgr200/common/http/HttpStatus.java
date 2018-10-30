package no.kristiania.pgr200.common.http;

import no.kristiania.pgr200.common.exceptions.InvalidHttpStatusCodeException;

public enum HttpStatus {
    Continue(100, "Continue"),
    SwitchingProtocols(101, "Switching Protocols"),
    Processing(102, "Processing"),
    OK(200, "OK"),
    Created(201, "Created"),
    Accepted(202, "Accepted"),
    NonAuthoritativeInformation(203, "Non-authoritative Information"),
    NoContent(204, "No Content"),
    ResetContent(205, "Reset Content"),
    PartialContent(206, "Partial Content"),
    MultiStatus(207, "Multi-Status"),
    AlreadyReported(208, "Already Reported"),
    IMUsed(226, "IM Used"),
    MultipleChoices(300, "Multiple Choices"),
    MovedPermanently(301, "Moved Permanently"),
    Found(302, "Found"),
    SeeOther(303, "See Other"),
    NotModified(304, "Not Modified"),
    UseProxy(305, "Use Proxy"),
    TemporaryRedirect(307, "Temporary Redirect"),
    PermanentRedirect(308, "Permanent Redirect"),
    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    PaymentRequired(402, "Payment Required"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found"),
    MethodNotAllowed(405, "Method Not Allowed"),
    NotAcceptable(406, "Not Acceptable"),
    ProxyAuthenticationRequired(407, "Proxy Authentication Required"),
    RequestTimeout(408, "Request Timeout"),
    Conflict(409, "Conflict"),
    Gone(410, "Gone"),
    LengthRequired(411, "Length Required"),
    PreconditionFailed(412, "Precondition Failed"),
    PayloadTooLarge(413, "Payload Too Large"),
    RequestURITooLong(414, "Request-URI Too Long"),
    UnsupportedMediaType(415, "Unsupported Media Type"),
    RequestedRangeNotSatisfiable(416, "Requested Range Not Satisfiable"),
    ExpectationFailed(417, "Expectation Failed"),
    ImaTeapot(418, "I'm a teapot"),
    MisdirectedRequest(421, "Misdirected Request"),
    UnprocessableEntity(422, "Unprocessable Entity"),
    Locked(423, "Locked"),
    FailedDependency(424, "Failed Dependency"),
    UpgradeRequired(426, "Upgrade Required"),
    PreconditionRequired(428, "Precondition Required"),
    TooManyRequests(429, "Too Many Requests"),
    RequestHeaderFieldsTooLarge(431, "Request Header Fields Too Large"),
    ConnectionClosedWithoutResponse(444, "Connection Closed Without Response"),
    UnavailableForLegalReasons(451, "Unavailable For Legal Reasons"),
    ClientClosedRequest(499, "Client Closed Request"),
    InternalServerError(500, "Internal Server Error"),
    NotImplemented(501, "Not Implemented"),
    BadGateway(502, "Bad Gateway"),
    ServiceUnavailable(503, "Service Unavailable"),
    GatewayTimeout(504, "Gateway Timeout"),
    HTTPVersionNotSupported(505, "HTTP Version Not Supported"),
    VariantAlsoNegotiates(506, "Variant Also Negotiates"),
    InsufficientStorage(507, "Insufficient Storage"),
    LoopDetected(508, "Loop Detected"),
    NotExtended(510, "Not Extended"),
    NetworkAuthenticationRequired(511, "Network Authentication Required"),
    NetworkConnectTimeoutError(599, "Network Connect Timeout Error");
    private final int statusCode;
    private final String realName;

    private HttpStatus(int statusCode, String realName) {
        this.statusCode = statusCode;
        this.realName = realName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String realName() {
        return realName;
    }

    public static HttpStatus valueOf(int statusCode) {
        switch (statusCode) {
            case 100:
                return Continue;
            case 101:
                return SwitchingProtocols;
            case 102:
                return Processing;
            case 200:
                return OK;
            case 201:
                return Created;
            case 202:
                return Accepted;
            case 203:
                return NonAuthoritativeInformation;
            case 204:
                return NoContent;
            case 205:
                return ResetContent;
            case 206:
                return PartialContent;
            case 207:
                return MultiStatus;
            case 208:
                return AlreadyReported;
            case 226:
                return IMUsed;
            case 300:
                return MultipleChoices;
            case 301:
                return MovedPermanently;
            case 302:
                return Found;
            case 303:
                return SeeOther;
            case 304:
                return NotModified;
            case 305:
                return UseProxy;
            case 307:
                return TemporaryRedirect;
            case 308:
                return PermanentRedirect;
            case 400:
                return BadRequest;
            case 401:
                return Unauthorized;
            case 402:
                return PaymentRequired;
            case 403:
                return Forbidden;
            case 404:
                return NotFound;
            case 405:
                return MethodNotAllowed;
            case 406:
                return NotAcceptable;
            case 407:
                return ProxyAuthenticationRequired;
            case 408:
                return RequestTimeout;
            case 409:
                return Conflict;
            case 410:
                return Gone;
            case 411:
                return LengthRequired;
            case 412:
                return PreconditionFailed;
            case 413:
                return PayloadTooLarge;
            case 414:
                return RequestURITooLong;
            case 415:
                return UnsupportedMediaType;
            case 416:
                return RequestedRangeNotSatisfiable;
            case 417:
                return ExpectationFailed;
            case 418:
                return ImaTeapot;
            case 421:
                return MisdirectedRequest;
            case 422:
                return UnprocessableEntity;
            case 423:
                return Locked;
            case 424:
                return FailedDependency;
            case 426:
                return UpgradeRequired;
            case 428:
                return PreconditionRequired;
            case 429:
                return TooManyRequests;
            case 431:
                return RequestHeaderFieldsTooLarge;
            case 444:
                return ConnectionClosedWithoutResponse;
            case 451:
                return UnavailableForLegalReasons;
            case 499:
                return ClientClosedRequest;
            case 500:
                return InternalServerError;
            case 501:
                return NotImplemented;
            case 502:
                return BadGateway;
            case 503:
                return ServiceUnavailable;
            case 504:
                return GatewayTimeout;
            case 505:
                return HTTPVersionNotSupported;
            case 506:
                return VariantAlsoNegotiates;
            case 507:
                return InsufficientStorage;
            case 508:
                return LoopDetected;
            case 510:
                return NotExtended;
            case 511:
                return NetworkAuthenticationRequired;
            case 599:
                return NetworkConnectTimeoutError;
            default:
                throw new InvalidHttpStatusCodeException();
        }
    }
}
