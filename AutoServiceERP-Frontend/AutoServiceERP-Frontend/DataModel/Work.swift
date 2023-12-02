//
//  Work.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 02.12.2023.
//

import Foundation

struct Work: Codable, Identifiable {
    var id: Int?
    var master: WorkMaster
    var car: WorkCar
    var service: WorkService
    var dateWork: Date
}

struct WorkMaster: Codable {
    var id: Int
}

struct WorkCar: Codable {
    var id: Int
}

struct WorkService: Codable {
    var id: Int
}

