//
//  SpravochnikiHomeView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 02.12.2023.
//

import SwiftUI

struct SpravochnikiHomeView: View {
    var body: some View {
        List {
            NavigationLink(destination: MasterListView()) {
                Text("Мастера")
            }
            NavigationLink(destination: CarListView()) {
                Text("Автомобили")
            }
            NavigationLink(destination: ServiceListView()) {
                Text("Услуги")
            }
        }
        .navigationTitle("Справочники")
    }
}
