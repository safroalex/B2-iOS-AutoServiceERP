//
//  Car.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//

import Foundation

struct Car: Codable, Identifiable {
    var id: Int?
    var num: String
    var color: String
    var mark: String
    var foreign: Bool
}
