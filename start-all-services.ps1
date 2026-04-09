# Start all lab microservices in order (Windows PowerShell).
# Prerequisites: MySQL running (e.g. XAMPP) with ratings_db; Java/Maven available via mvnw.
# Usage: powershell -ExecutionPolicy Bypass -File .\start-all-services.ps1

param(
    [int]$EurekaTimeoutSeconds = 120,
    [int]$AfterWorkersSleepSeconds = 12
)

$ErrorActionPreference = 'Continue'

$RepoRoot = if ($PSScriptRoot) { $PSScriptRoot } else { Split-Path -Parent $MyInvocation.MyCommand.Path }

function Wait-ForHttp {
    param(
        [string]$Uri,
        [int]$TimeoutSeconds
    )
    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    Write-Host "Waiting for $Uri (timeout ${TimeoutSeconds}s) ..." -ForegroundColor Yellow
    while ((Get-Date) -lt $deadline) {
        try {
            $null = Invoke-WebRequest -Uri $Uri -UseBasicParsing -TimeoutSec 3 -ErrorAction Stop
            Write-Host "Reachable: $Uri" -ForegroundColor Green
            return $true
        } catch {
            Start-Sleep -Seconds 2
        }
    }
    Write-Host "Timeout: $Uri did not respond in time. Continuing anyway; workers may fail until Eureka is up." -ForegroundColor Red
    return $false
}

function Start-SpringService {
    param(
        [string]$DisplayName,
        [string]$ModuleRelativePath
    )
    $dir = Join-Path $RepoRoot $ModuleRelativePath
    $mvnw = Join-Path $dir 'mvnw.cmd'
    if (-not (Test-Path -LiteralPath $mvnw)) {
        Write-Host "Missing $mvnw - skip $DisplayName" -ForegroundColor Red
        return
    }
    Write-Host "Starting $DisplayName ..." -ForegroundColor Cyan
    $dirLiteral = $dir.Replace("'", "''")
    Start-Process -FilePath 'powershell.exe' -ArgumentList @(
        '-NoExit',
        '-Command',
        "Set-Location -LiteralPath '$dirLiteral'; & .\mvnw.cmd spring-boot:run"
    ) | Out-Null
}

Write-Host ""
Write-Host "Repo: $RepoRoot" -ForegroundColor Gray
Write-Host "1) Discovery Server (Eureka) on :8761" -ForegroundColor White
Write-Host "2) Ratings, Movie Info, Trending (parallel)" -ForegroundColor White
Write-Host "3) Movie Catalog (after workers)" -ForegroundColor White
Write-Host ""

# --- 1) Eureka first ---
Start-SpringService -DisplayName 'discovery-server' -ModuleRelativePath 'discovery-server'
Wait-ForHttp -Uri 'http://localhost:8761/' -TimeoutSeconds $EurekaTimeoutSeconds | Out-Null

# --- 2) Downstream services (order among them does not matter much) ---
Start-SpringService -DisplayName 'ratings-data-service' -ModuleRelativePath 'ratings-data-service'
Start-SpringService -DisplayName 'movie-info-service' -ModuleRelativePath 'movie-info-service'
Start-SpringService -DisplayName 'trending-movies-service' -ModuleRelativePath 'trending-movies-service'

Write-Host "Sleeping ${AfterWorkersSleepSeconds}s so services can register ..." -ForegroundColor Yellow
Start-Sleep -Seconds $AfterWorkersSleepSeconds

# --- 3) Catalog depends on others ---
Start-SpringService -DisplayName 'movie-catalog-service' -ModuleRelativePath 'movie-catalog-service'

Write-Host ""
Write-Host "Done. Each service runs in its own window. Close those windows to stop." -ForegroundColor Green
Write-Host "Catalog: http://localhost:8081/catalog/..." -ForegroundColor Green
Write-Host ""
