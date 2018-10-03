#
# Filename:    Makefile
# Author:      Zeliha Ayar
# Datred:      May 17, 2017
# Decription:  Makefile for the CompilerDesign 
#

SHELL := /bin/sh
MAKE  := make
ROOT  := ./compiler

# Directories
INCLUDES := ./khudi
BIN_DIR  := ./bin
CC      := javac
RM      := rm
JAR     := jar
JFLAGS  := cmfv
CFLAGS  := -Xlint
LDFLAGS :=
LIBS    :=
.SUFFIXES: .java .class
MANIFEST_FILE := manifest.txt

echo_cmd = @echo   "  $(1)$(ECHO_FILE_NAME)" ; $(2)

MAKE := $(MAKE) --no-print-directory --silent
CC   := $(call echo_cmd,CC      ,$(CC))
LD   := $(call echo_cmd,LD      ,$(LD))
RM   := $(call echo_cmd,RM      ,$(RM))

usage:
	@echo ""
	@echo "Master Makefile for Compiler."
	@echo ""
	@echo "Usage:"
	@echo "	make all        # Compile everything"
	@echo "	make clean      # Remove all object files"
	@echo "	make clean_all  # Remove all object files and the binary"
	@echo ""

SIGNATURE_DIR = $(ROOT)/lexical
SIGNATURE_DIR_1 = $(ROOT)/semantic
SIGNATURE_DIR_2 = $(ROOT)/codegen
MAIN_DIR      = $(ROOT)

SRCS =	$(SIGNATURE_DIR)/Lexical.java \
        $(SIGNATURE_DIR_1)/Semantic.java \
		$(SIGNATURE_DIR_2)/Codegen.java \
        $(MAIN_DIR)/def.java \
        $(MAIN_DIR)/main.java

all: $(SRCS) compiler.jar

OBJS = $(SRCS:.java=.class)

compiler.jar:  $(OBJS)
	$(JAR) $(JFLAGS) $(MANIFEST_FILE) compiler.jar $(OBJS)

.java.class:
	$(CC) $(CFLAGS) $<

clean:
	$(RM) $(OBJS)

clean_all:
	$(RM) $(OBJS) $(BIN_DIR)/compiler.jar
