package ru.numismatics.backend.api.v1

class UnknownRequestClass(clazz: Class<*>) :
    RuntimeException("Class $clazz cannot be mapped to NumismaticsPlatformContext")