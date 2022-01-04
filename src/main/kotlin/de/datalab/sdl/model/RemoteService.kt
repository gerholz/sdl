package de.datalab.sdl.model


/*
Copyright 2022 Gerhard Holzmeister
*/

class RemoteService(namespace: Namespace, name: String, methods: List<MethodType>, val remoteServiceData: RemoteServiceData): InterfaceType(namespace, name, methods) {

}