# CoTrip Android Development Makefile
# Automates emulator and app management

# Variables
AVD_NAME = CoTrip_AVD
EMULATOR_TIMEOUT = 60
APP_PACKAGE = com.cotrip.mobile

# Colors for output
RED = \033[0;31m
GREEN = \033[0;32m
YELLOW = \033[1;33m
BLUE = \033[0;34m
NC = \033[0m # No Color

.PHONY: help start-dev stop-emulator clean build install run status logs

# Default target
help:
	@echo "$(BLUE)CoTrip Android Development Commands:$(NC)"
	@echo "  $(GREEN)start-dev$(NC)     - Start emulator (if needed) and install/run the app"
	@echo "  $(GREEN)build$(NC)         - Build the app"
	@echo "  $(GREEN)install$(NC)       - Install the app to connected device/emulator"
	@echo "  $(GREEN)run$(NC)           - Launch the app on device/emulator"
	@echo "  $(GREEN)clean$(NC)         - Clean build artifacts"
	@echo "  $(GREEN)stop-emulator$(NC) - Stop the emulator"
	@echo "  $(GREEN)status$(NC)        - Show emulator and device status"
	@echo "  $(GREEN)logs$(NC)          - Show app logs"

# Main development target - starts everything
start-dev: check-emulator-and-start install run
	@echo "$(GREEN)✅ Development environment ready!$(NC)"

# Check if emulator is running, start if needed
check-emulator-and-start:
	@echo "$(BLUE)🔍 Checking emulator status...$(NC)"
	@if adb devices | grep -q "emulator-"; then \
		echo "$(GREEN)✅ Emulator is already running$(NC)"; \
	else \
		echo "$(YELLOW)⚡ Starting emulator...$(NC)"; \
		$(MAKE) start-emulator-and-wait; \
	fi

# Start emulator and wait for it to be ready
start-emulator-and-wait:
	@echo "$(BLUE)🚀 Starting $(AVD_NAME) emulator...$(NC)"
	@emulator -avd $(AVD_NAME) -no-snapshot-load > /dev/null 2>&1 & \
	echo "$(YELLOW)⏳ Waiting for emulator to boot (max $(EMULATOR_TIMEOUT)s)...$(NC)"; \
	timeout=$(EMULATOR_TIMEOUT); \
	while [ $$timeout -gt 0 ]; do \
		if adb devices | grep -q "device$$"; then \
			echo "$(GREEN)✅ Emulator is ready!$(NC)"; \
			break; \
		fi; \
		echo -n "$(YELLOW).$(NC)"; \
		sleep 2; \
		timeout=$$((timeout-2)); \
	done; \
	if [ $$timeout -le 0 ]; then \
		echo "$(RED)❌ Emulator failed to start within $(EMULATOR_TIMEOUT) seconds$(NC)"; \
		exit 1; \
	fi

# Stop emulator
stop-emulator:
	@echo "$(BLUE)🛑 Stopping emulator...$(NC)"
	@pkill -f "emulator -avd" || echo "$(YELLOW)No emulator process found$(NC)"
	@echo "$(GREEN)✅ Emulator stopped$(NC)"

# Clean build
clean:
	@echo "$(BLUE)🧹 Cleaning build artifacts...$(NC)"
	@./gradlew clean
	@echo "$(GREEN)✅ Clean complete$(NC)"

# Build the app
build:
	@echo "$(BLUE)🔨 Building app...$(NC)"
	@./gradlew assembleDebug
	@echo "$(GREEN)✅ Build complete$(NC)"

# Install the app
install:
	@echo "$(BLUE)📱 Installing app...$(NC)"
	@if ! adb devices | grep -q "device$$"; then \
		echo "$(RED)❌ No device/emulator connected$(NC)"; \
		exit 1; \
	fi
	@./gradlew installDebug
	@echo "$(GREEN)✅ App installed$(NC)"

# Run/launch the app
run:
	@echo "$(BLUE)🚀 Launching app...$(NC)"
	@if ! adb devices | grep -q "device$$"; then \
		echo "$(RED)❌ No device/emulator connected$(NC)"; \
		exit 1; \
	fi
	@adb shell am start -n $(APP_PACKAGE)/.MainActivity
	@echo "$(GREEN)✅ App launched$(NC)"

# Show status
status:
	@echo "$(BLUE)📊 System Status:$(NC)"
	@echo "$(YELLOW)Connected Devices:$(NC)"
	@adb devices
	@echo ""
	@echo "$(YELLOW)Available AVDs:$(NC)"
	@emulator -list-avds
	@echo ""
	@echo "$(YELLOW)Running Emulator Processes:$(NC)"
	@pgrep -fl emulator || echo "No emulator processes running"

# Show app logs
logs:
	@echo "$(BLUE)📋 App Logs (press Ctrl+C to stop):$(NC)"
	@adb logcat -s $(APP_PACKAGE),AndroidRuntime:E

# Quick restart - stop app, reinstall, and run
restart: stop-app install run
	@echo "$(GREEN)✅ App restarted$(NC)"

# Stop the app
stop-app:
	@echo "$(BLUE)🛑 Stopping app...$(NC)"
	@adb shell am force-stop $(APP_PACKAGE) || echo "$(YELLOW)App not running$(NC)"

# Full rebuild and restart
full-restart: clean build install run
	@echo "$(GREEN)✅ Full restart complete$(NC)"

# Check if required tools are available
check-tools:
	@echo "$(BLUE)🔧 Checking required tools...$(NC)"
	@command -v adb >/dev/null 2>&1 || { echo "$(RED)❌ adb not found$(NC)"; exit 1; }
	@command -v emulator >/dev/null 2>&1 || { echo "$(RED)❌ emulator not found$(NC)"; exit 1; }
	@command -v ./gradlew >/dev/null 2>&1 || { echo "$(RED)❌ gradlew not found$(NC)"; exit 1; }
	@echo "$(GREEN)✅ All tools available$(NC)"

# Development workflow - clean, build, and start
dev: check-tools clean start-dev
	@echo "$(GREEN)🎉 Development workflow complete!$(NC)"
