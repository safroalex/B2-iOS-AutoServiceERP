//
//  Service.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//

import Foundation

struct Service: Codable, Identifiable {
    var id: Int?
    var name: String
    var costOur: Double
    var costForeign: Double
}
