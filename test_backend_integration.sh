#!/bin/bash

# Backend AI 연동 테스트 스크립트

BACKEND_URL="http://localhost:8080"

echo "================================================================================"
echo "🚀 Backend AI 연동 테스트"
echo "================================================================================"
echo "📡 Backend URL: $BACKEND_URL"
echo ""

# 1. Backend 서버 헬스체크
echo "================================================================================"
echo "🧪 1. Backend 서버 헬스체크"
echo "================================================================================"

if curl -s "$BACKEND_URL/actuator/health" > /dev/null 2>&1; then
    echo "✅ Backend 서버 정상 작동"
else
    echo "❌ Backend 서버에 연결할 수 없습니다."
    echo "💡 Backend 서버를 시작하세요:"
    echo "   cd backend && ./gradlew bootRun"
    exit 1
fi

echo ""

# 2. 요트 이름으로 부품 조회
echo "================================================================================"
echo "🧪 2. 요트 이름으로 부품 조회 (J/70)"
echo "================================================================================"

RESPONSE=$(curl -s "$BACKEND_URL/api/yacht/part-list?name=J/70")

if echo "$RESPONSE" | grep -q '"success"'; then
    echo "✅ 요트 부품 조회 성공"
    echo ""
    echo "📦 응답 데이터:"
    echo "$RESPONSE" | python -m json.tool 2>/dev/null || echo "$RESPONSE"
else
    echo "❌ 요트 부품 조회 실패"
    echo "   응답: $RESPONSE"
fi

echo ""

# 3. PDF 파일 업로드 테스트 (선택사항)
if [ -n "$1" ]; then
    echo "================================================================================"
    echo "🧪 3. PDF 파일 업로드 테스트"
    echo "================================================================================"
    echo "📄 파일: $1"
    
    if [ ! -f "$1" ]; then
        echo "❌ 파일을 찾을 수 없습니다: $1"
    else
        RESPONSE=$(curl -s -X POST "$BACKEND_URL/api/yacht/part-list" \
            -F "name=Test Yacht" \
            -F "files=@$1")
        
        if echo "$RESPONSE" | grep -q '"success"'; then
            echo "✅ PDF 업로드 및 분석 성공"
            echo ""
            echo "📦 응답 데이터:"
            echo "$RESPONSE" | python -m json.tool 2>/dev/null || echo "$RESPONSE"
        else
            echo "❌ PDF 업로드 실패"
            echo "   응답: $RESPONSE"
        fi
    fi
else
    echo "================================================================================"
    echo "ℹ️  PDF 테스트를 건너뜁니다"
    echo "================================================================================"
    echo "💡 PDF 테스트를 하려면 다음과 같이 실행하세요:"
    echo "   ./test_backend_integration.sh path/to/your/file.pdf"
fi

echo ""
echo "================================================================================"
echo "✅ 테스트 완료!"
echo "================================================================================"
echo ""
echo "💡 다음 단계:"
echo "   1. Flutter 앱에서 Backend API 호출 테스트"
echo "   2. 실제 배포 환경에서 테스트"
echo "   3. 성능 모니터링 및 최적화"
echo ""
echo "================================================================================"

